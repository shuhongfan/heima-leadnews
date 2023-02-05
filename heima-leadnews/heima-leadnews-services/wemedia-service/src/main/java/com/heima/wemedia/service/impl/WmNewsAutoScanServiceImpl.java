package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.aliyun.scan.GreenScan;
import com.heima.aliyun.scan.ScanResult;
import com.heima.common.constants.message.PublishArticleConstants;
import com.heima.common.constants.wemedia.WemediaConstants;
import com.heima.common.exception.CustException;
import com.heima.feigns.AdminFeign;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.utils.common.SensitiveWordUtil;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.service.WmNewsAutoScanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WmNewsAutoScanServiceImpl implements WmNewsAutoScanService {

    @Autowired
    private WmNewsMapper wmNewsMapper;

    @Value("${file.oss.web-site}")
    private String webSite;

    @Autowired
    private AdminFeign adminFeign;

    @Autowired
    private GreenScan greenScan;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 自媒体文章审核
     * @param id  自媒体文章id
     */
    @Override
    public void autoScanWmNews(Integer id) {
        log.info("文章自动审核方法触发  待审核的文章id：{}", id);
//        1. 判断文章id是否为空
        if (id == null) {
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID, "待审核的文章id为空");
        }

//        2. 根据id查询自媒体文章
        WmNews wmNews = wmNewsMapper.selectById(id);
        if (wmNews == null) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST, "对于文章不存在");
        }

//        3.判断文章的状态 必须是1（待审核） 避免重复消费
        Short status = wmNews.getStatus();
        if (!WemediaConstants.WM_NEWS_SUMMIT_STATUS.equals(status)) {
            log.info("当前文章状态为：{} 不是待审核状态，无需审核", status);
            return;
        }

//        4. 抽取文章中所有的本文内容和所有的图片内容
        Map<String, Object> contentAndImages = handleTextAndImages(wmNews);

//        5. DFA进行自管理敏感词审核  2有敏感词不通过
        boolean scanSensitive = handleSensitive((String)contentAndImages.get("content"), wmNews);
        if (!scanSensitive) {
            log.info("文章审核未通过，原因：内容中包含敏感词");
            return;
        }

//        6. 阿里云的文本审核 2有违规词汇  3 不确定/未调用成功，进步人工审核
        boolean scanText = handleTextScan((String) contentAndImages.get("content"), wmNews);
        if (!scanText) {
//            返回false代表审核不成功  结束处理
            log.info("文章审核未通过,原因:内容包含违规词汇");
            return;
        }

//        7. 阿里云的图片审核 2有违规图片  3 不确定/未调用成功，进步人工审核
        boolean scanImage = handleImageScan((List<String>) contentAndImages.get("images"), wmNews);
        if (!scanImage) {
            log.info("文章审核未通过,原因:图片中有敏感信息");
            return;
        }

//        8. 将文章的状态改为8 自动审核通过
        updateWmNews(WmNews.Status.SUCCESS.getCode(), "审核成功",wmNews);

//        9. 根据文章的发布时间，发送延迟消息，用于定时发布文章
//        获取发布时间
        long publishTime = wmNews.getPublishTime().getTime();

//        获取当前时间
        long nowTime = System.currentTimeMillis();

//        发布时间 -  当前时间 = 距离发布的延迟时间
        long remainTime = publishTime - nowTime;

//        使用rabbittemplate发送延迟消息
        rabbitTemplate.convertAndSend(
                PublishArticleConstants.DELAY_DIRECT_EXCHANGE,
                PublishArticleConstants.PUBLISH_ARTICLE_ROUTE_KEY,
                wmNews.getId(),
                new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        MessageProperties messageProperties = message.getMessageProperties();
//                        设置消息头
                        messageProperties.setHeader("x-delay", remainTime <= 0 ? 0 : remainTime);
                        return message;
                    }
                }
        );
        log.info("成功发送 文章延迟发布消息  文章id:{}  当前时间:{}", wmNews.getId(), LocalDateTime.now());
    }

    /**
     * 7. 阿里云的图片审核 2有违规图片  3 不确定/未调用成功，进步人工审核
     * @param images
     * @param wmNews
     * @return
     */
    private boolean handleImageScan(List<String> images, WmNews wmNews) {
        boolean flag = false;
        ScanResult scanResult = null;
        try {
            scanResult = greenScan.imageUrlScan(images);
//            阿里云建议: block 违规 review 进一步人工审核 pass通过
            String suggestion = scanResult.getSuggestion();
            switch (suggestion) {
                case "block":
//                    失败
                    updateWmNews(WmNews.Status.FAIL.getCode(), "图片中包含违规信息:"+scanResult.getLabel(),wmNews);
                    break;
                case "review":
                    updateWmNews(WmNews.Status.ADMIN_AUTH.getCode(), "图片中有不确定因素,需要进步审核",wmNews);
//                    人工
                    break;
                case "pass":
//                    成功
                    flag = true;
                    break;
                default:
//                    人工
                    updateWmNews(WmNews.Status.ADMIN_AUTH.getCode(), "阿里云调用状态异常,图片中有不确定因素,需要进步审核",wmNews);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
//            人工
            updateWmNews(WmNews.Status.ADMIN_AUTH.getCode(), "阿里云调用失败,图片内容中有不确定因素,需要进步审核",wmNews);
        }
        return flag;
    }

    /**
     * 6. 阿里云的文本审核 2有违规词汇  3 不确定/未调用成功，进步人工审核
     * @param content
     * @param wmNews
     * @return
     */
    private boolean handleTextScan(String content, WmNews wmNews) {
        boolean flag = false;
        ScanResult scanResult = null;
        try {
            scanResult = greenScan.greenTextScan(content);
//            阿里云建议: block 违规 review 进一步人工审核 pass通过
            String suggestion = scanResult.getSuggestion();
            switch (suggestion) {
                case "block":
//                    失败
                    updateWmNews(WmNews.Status.FAIL.getCode(), "文章中包含违规词汇:"+scanResult.getLabel(),wmNews);
                    break;
                case "review":
                    updateWmNews(WmNews.Status.ADMIN_AUTH.getCode(), "文章内容中有不确定因素,需要进步审核",wmNews);
//                    人工
                    break;
                case "pass":
//                    成功
                    flag = true;
                    break;
                default:
//                    人工
                    updateWmNews(WmNews.Status.ADMIN_AUTH.getCode(), "阿里云调用状态异常,文章内容中有不确定因素,需要进步审核",wmNews);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
//            人工
            updateWmNews(WmNews.Status.ADMIN_AUTH.getCode(), "阿里云调用失败,文章内容中有不确定因素,需要进步审核",wmNews);
        }
        return flag;
    }

    /**
     * DFA进行自管理敏感词审核  2有敏感词不通过
     * @param content
     * @param wmNews
     * @return
     */
    private boolean handleSensitive(String content, WmNews wmNews) {
        boolean flag = true;
//        1.远程查询敏感词列表
        ResponseResult<List<String>> sensitives = adminFeign.sensitives();
        if (!sensitives.checkCode()) {
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR);
        }
        List<String> sensitivesData = sensitives.getData();

//        2. 将敏感词 转为DFA数据模型
        SensitiveWordUtil.initMap(sensitivesData);

//        3. 基于DFA扫描 内容中是否包含敏感词
        Map<String, Integer> map = SensitiveWordUtil.matchWords(content);

//        4. 如果有敏感词,将文章状态修改为2,并告知原因
        if (!CollectionUtils.isEmpty(map)) {
//            修改文章状态
            updateWmNews(WmNews.Status.FAIL.getCode(), "文章中包含敏感词:" + map, wmNews);
//            审核失败
            flag = false;
        }
        return flag;
    }

    /**
     * 修改文章状态
     * @param status
     * @param reason
     * @param wmNews
     */
    private void updateWmNews(Short status,String reason,WmNews wmNews) {
        wmNews.setStatus(status);
        wmNews.setReason(reason);
        wmNewsMapper.updateById(wmNews);
    }

    /**
     * 抽取文章中所有的本文内容和所有的图片内容
     * @param wmNews
     * @return
     */
    private Map<String, Object> handleTextAndImages(WmNews wmNews) {
        HashMap<String, Object> result = new HashMap<>();

//        1. 判断 内容 不能为空 并转为 List<Map>
        String contentStr = wmNews.getContent();
        if (StringUtils.isBlank(contentStr)) {
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID, "文章内容不能为空");
        }
        List<Map> contentMapList = JSON.parseArray(contentStr, Map.class);

//        2. 抽取文章内容中所有的文本内容 拼接成一个字符串
//        2.1 抽取content中所有文本 并拼接成一个字符串
        String content = contentMapList.stream().filter(item -> item.get("type").equals("text"))
                .map(item -> item.get("value").toString())
                .collect(Collectors.joining("_hmtt_"));

//        2.2 将文本内容和标题拼接成一个字符串
        content += wmNews.getTitle() + "_hmtt_";

//        2.3 将总体的文本内容装入map
        result.put("content", content);

//        3 抽取文章中所有的图片列表
//        3.1 抽取content中所有的图片 得到图片列表
        List<String> images = contentMapList.stream().filter(item -> item.get("type").equals("image"))
                .map(item -> item.get("value").toString())
                .collect(Collectors.toList());

//        3.2 抽取封面中所有的图片 得到图片列表
        String coverStr = wmNews.getImages();
        if (StringUtils.isNotBlank(coverStr)) {
            String[] strings = coverStr.split(",");
            List<String> coverImages = Arrays.stream(strings).map(url -> webSite + url).collect(Collectors.toList());
//        3.3 合并 内容图片 和 封面图片
            images.addAll(coverImages);
        }

//        3.4 去除重复图片
        images = images.stream().distinct().collect(Collectors.toList());

//        3.5 将所有图片 装入map
        result.put("images", images);
        return result;
    }
}
