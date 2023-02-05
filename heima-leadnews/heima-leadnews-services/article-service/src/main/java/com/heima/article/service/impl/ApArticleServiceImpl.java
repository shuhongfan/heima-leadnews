package com.heima.article.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.mapper.AuthorMapper;
import com.heima.article.service.ApArticleService;
import com.heima.common.exception.CustException;
import com.heima.feigns.AdminFeign;
import com.heima.feigns.WemediaFeign;
import com.heima.model.admin.pojo.AdChannel;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.article.pojos.ApArticleContent;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.pojos.WmNews;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {

    @Autowired
    private WemediaFeign wemediaFeign;

    @Autowired
    private AdminFeign adminFeign;

    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;

    /**
     * 保存或修改文章
     * @param newsId 文章id
     * @return
     */
    @Override
    @GlobalTransactional(rollbackFor = Exception.class,timeoutMills = 3000000)
    public void publishArticle(Integer newsId) {
//        1.根据id查询并校验自媒体文章
        WmNews wmNews = getWmNews(newsId);

//        2. 根据wmNews封装 apArticle对象
        ApArticle apArticle = getApArticle(wmNews);

//        3.保存或修改 apArticle
        saveOrUpdateArticle(apArticle);

//        4.保存关联的配置和内容信息
        saveConfigAndContent(wmNews, apArticle);

//        5. 基于新的文章内容生成HTML静态页

//        6. 更新wmNews状态为9
        updateWmNews(wmNews, apArticle);

//        7. 通过es 更新索引库

    }

    /**
     * 6. 更新wmNews状态为9
     * @param wmNews
     * @param apArticle
     */
    private void updateWmNews(WmNews wmNews, ApArticle apArticle) {
//        将文章改为发布状态
        wmNews.setStatus(WmNews.Status.PUBLISHED.getCode());

//        关联articleId
        wmNews.setArticleId(apArticle.getId());

//        远程调用
        ResponseResult responseResult = wemediaFeign.updateWmNews(wmNews);
        if (!responseResult.checkCode()) {
            log.error("远程调用修改自媒体文章接口失败:{}",responseResult.getErrorMessage());
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR,responseResult.getErrorMessage());
        }
    }

    /**
     *  4.保存关联的配置和内容信息
     * @param wmNews
     * @param apArticle
     */
    private void saveConfigAndContent(WmNews wmNews, ApArticle apArticle) {
//        1. 保存配置
        ApArticleConfig apArticleConfig = new ApArticleConfig();
        apArticleConfig.setArticleId(apArticle.getId());
//        是否允许评论
        apArticleConfig.setIsComment(true);
//        是否允许转发
        apArticleConfig.setIsForward(true);
//        是否 下架
        apArticleConfig.setIsDown(false);
//        是否 删除
        apArticleConfig.setIsDelete(false);
        apArticleConfigMapper.insert(apArticleConfig);

//        2. 文章设置
        ApArticleContent apArticleContent = new ApArticleContent();
        apArticleContent.setArticleId(apArticle.getId());
        apArticleContent.setContent(wmNews.getContent());
        apArticleContentMapper.insert(apArticleContent);
    }

    /**
     * 3.保存或修改文章 apArticle
     * @param apArticle
     */
    private void saveOrUpdateArticle(ApArticle apArticle) {
//        1.判断文章id是否存在
        if (apArticle.getId() == null) {
//        2 如果不存在 保存文章
            apArticle.setLikes(0);
            apArticle.setViews(0);
            apArticle.setComment(0);
            apArticle.setCollection(0);
            save(apArticle);
        } else {
//        3.如果存在 修改文章
//        3.1 查询之前的article是否存在
            ApArticle oldArticle = getById(apArticle.getId());
            if (oldArticle == null) {
                CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST, "关联的文章数据不存在");
            }

//            3.2 修改文章
            updateById(apArticle);

//            3.3 删除之前所关联的 config 和content
            apArticleConfigMapper.delete(Wrappers.<ApArticleConfig>lambdaQuery().eq(ApArticleConfig::getArticleId, apArticle.getId()));
            apArticleContentMapper.delete(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId, apArticle.getId()));
        }

    }

    /**
     * 2.根据wmNews封装 apArticle对象
     * @param wmNews
     * @return
     */
    private ApArticle getApArticle(WmNews wmNews) {
//        1. 创建article对象
        ApArticle apArticle = new ApArticle();

//        2.拷贝属性 wmNews==>article
        BeanUtils.copyProperties(wmNews, apArticle);

//        3.拷贝其他属性  id flag layout
        apArticle.setId(wmNews.getArticleId());
        apArticle.setFlag((byte) 0);
        apArticle.setLayout(wmNews.getType());

//        4. 补全频道信息
        ResponseResult<AdChannel> channelResult = adminFeign.findOne(wmNews.getChannelId());
        if (!channelResult.checkCode()) {
            log.error("远程调用查询接口失败:{}", channelResult.getErrorMessage());
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR, channelResult.getErrorMessage());
        }
        AdChannel channel = channelResult.getData();
        if (channel == null) {
            log.error("远程查询频道信息为null  频道id:{}", wmNews.getChannelId());
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST, "频道不存在");
        }
        apArticle.setChannelName(channel.getName());

//        5 补全作者信息
        ApAuthor apAuthor = authorMapper.selectOne(Wrappers.<ApAuthor>lambdaQuery().eq(ApAuthor::getWmUserId, wmNews.getUserId()));
        if (apAuthor == null) {
            log.error("关联的作者信息不存在 wmUserId:{}", wmNews.getUserId());
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST, "关联的作者信息不存在");
        }
        apArticle.setAuthorId(apAuthor.getId().longValue());
        apArticle.setAuthorName(apArticle.getAuthorName());

        return apArticle;
    }

    /**
     * 1.根据id查询并校验自媒体文章
     * @param newsId
     * @return
     */
    private WmNews getWmNews(Integer newsId) {
//        1. 根据id远程查询自媒体文章
        ResponseResult<WmNews> result = wemediaFeign.findWmNewsById(newsId);

//        2. 检查远程调用状态
        if (!result.checkCode()) {
            log.error("远程调用自媒体文章信息失败 文章id:{}", newsId);
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR, result.getErrorMessage());
        }

//        3.判断news是否为null
        WmNews wmNews = result.getData();
        if (wmNews == null) {
            log.error("远程调用自媒体文章信息失败 文章id:{}", newsId);
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST, "对于的文章信息不存在 文章id:" + newsId);
        }

//        4. 检查news是否为 4 或 8
        short status = wmNews.getStatus().shortValue();
        if (WmNews.Status.ADMIN_SUCCESS.getCode() != status && WmNews.Status.SUCCESS.getCode() != status) {
            log.error("对应的文章状态不为 审核通过,无法发布, 文章id:{}", newsId);
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST, "对应的文章状态不为 审核通过,无法发布, 文章id:" + newsId);
        }
        return wmNews;
    }
}
