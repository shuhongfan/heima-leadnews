package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.exception.CustException;
import com.heima.model.common.constants.message.NewsAutoScanConstants;
import com.heima.model.common.constants.message.NewsUpOrDownConstants;
import com.heima.model.common.constants.message.PublishArticleConstants;
import com.heima.model.common.constants.wemedia.WemediaConstants;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.threadlocal.WmThreadLocalUtils;
import com.heima.model.wemedia.dtos.NewsAuthDTO;
import com.heima.model.wemedia.dtos.WmNewsDTO;
import com.heima.model.wemedia.dtos.WmNewsPageReqDTO;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmNewsMaterial;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.model.wemedia.vos.WmNewsVO;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.mapper.WmUserMapper;
import com.heima.wemedia.service.WmNewsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mrchen
 * @date 2022/4/25 11:07
 */
@Service
@Slf4j
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {
    @Value("${file.oss.web-site}")
    private String webSite;

    @Override
    public ResponseResult findList(WmNewsPageReqDTO dto) {
        // 1. 校验参数   分页   登陆
        dto.checkParam();
        WmUser user = WmThreadLocalUtils.getUser();
        if (user == null) {
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN);
        }
        // 2. 封装查询条件
        // 2.1  分页条件
        Page<WmNews> pageReq = new Page<>(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<WmNews> queryWrapper = Wrappers.lambdaQuery();
        // 2.2  如果标题，模糊查询title
//        if(StringUtils.isNotBlank(dto.getKeyword())){
//        }
        queryWrapper.like(StringUtils.isNotBlank(dto.getKeyword()),WmNews::getTitle,dto.getKeyword());
        // 2.3 如果有频道id  查询channel_id
        queryWrapper.eq(dto.getChannelId()!=null,WmNews::getChannelId,dto.getChannelId());
        // 2.4 如果有status   按照状态查询
        queryWrapper.eq(dto.getStatus()!=null,WmNews::getStatus,dto.getStatus());
        // 2.5 如果有开始时间  按照 > = 开始时间
        queryWrapper.ge(dto.getBeginPubDate()!=null,WmNews::getPublishTime,dto.getBeginPubDate());
        // 2.6 如果有结束时间  按照 < = 结束时间
        queryWrapper.le(dto.getEndPubDate()!=null,WmNews::getPublishTime,dto.getEndPubDate());
        // 2.7 按照登陆用户id查询
        queryWrapper.eq(WmNews::getUserId,user.getId());
        // 2.8 按照发布时间降序
        queryWrapper.orderByDesc(WmNews::getPublishTime);
        // 3. 执行查询得到返回结果   code msg data  host: 设置图片访问前缀
        IPage<WmNews> pageResult = this.page(pageReq, queryWrapper);
        PageResponseResult result = new PageResponseResult(dto.getPage(), dto.getSize(), pageResult.getTotal(), pageResult.getRecords());
        result.setHost(webSite);
        return result;
    }

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult submitNews(WmNewsDTO dto) {
        // 1.  检查参数 将 dto 封装成 wmNews对象
        if(StringUtils.isBlank(dto.getContent())||StringUtils.isBlank(dto.getTitle())){
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"文章标题 或 内容 不能为空");
        }
        WmUser user = WmThreadLocalUtils.getUser();
        if (user == null) {
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN);
        }
        WmNews wmNews = new WmNews();
        BeanUtils.copyProperties(dto,wmNews);
        if (WemediaConstants.WM_NEWS_TYPE_AUTO.equals(dto.getType())) {
            // 数据库中的type值不能为负数，如果是-1 自动生成 暂时设置为null
            wmNews.setType(null);
        }
        List<String> images = dto.getImages();
        if(!CollectionUtils.isEmpty(images)){
            // 将封面集合转为字符串
            wmNews.setImages(imagesToStr(dto.getImages()));
        }
        // 发表文章的用户
        wmNews.setUserId(user.getId());
        // 2.  保存或修改  wmNews
        saveWmNews(wmNews);
        if(WemediaConstants.WM_NEWS_DRAFT_STATUS.equals(dto.getStatus())){
            // 如果是草稿 ，不用保存关联关系
            return ResponseResult.okResult();
        }
        // 3. 判断如果是提交待审核状态保存素材和文章的关联关系
        // 3.1   解析出 内容当中所引用的素材列表
        List<String> urlList = parseContentImages(dto.getContent());
        // 3.2   保存文章内容 和 素材的关联关系
        if(!CollectionUtils.isEmpty(urlList)){
            saveRelativeInfo(urlList,wmNews.getId(),WemediaConstants.WM_CONTENT_REFERENCE);
        }
        // 3.3 保存文章封面 和 素材的关联关系
        saveRelativeInfoForCover(urlList,wmNews,dto);

        // 3.4 发送待审核消息
        rabbitTemplate.convertAndSend(NewsAutoScanConstants.WM_NEWS_AUTO_SCAN_QUEUE,wmNews.getId());
        log.info("成功发送待审核消息，待审核的文章id为: {}",wmNews.getId());
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult findWmNewsById(Integer id) {
        //1 参数检查
        if (id == null) {
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2 执行查询
        WmNews wmNews = getById(id);
        if (wmNews == null) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //3 返回结果
        ResponseResult result = ResponseResult.okResult(wmNews);
        result.setHost(webSite);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult delNews(Integer id) {
        //1.检查参数
        if(id == null){
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"文章Id不可缺少");
        }
        //2.获取数据
        WmNews wmNews = getById(id);
        if(wmNews == null){
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"文章不存在");
        }
        //3.判断当前文章的状态  status==9  enable == 1
        if(wmNews.getStatus().equals(WmNews.Status.PUBLISHED.getCode())
                && wmNews.getEnable().equals(WemediaConstants.WM_NEWS_UP)){
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"文章已发布，不能删除");
        }
        //4.去除素材与文章的关系
        wmNewsMaterialMapper.delete(Wrappers.<WmNewsMaterial>lambdaQuery().eq(WmNewsMaterial::getNewsId,wmNews.getId()));
        //5.删除文章
        removeById(wmNews.getId());
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult downOrUp(WmNewsDTO dto) {
        //1.检查参数
        if(dto == null || dto.getId() == null){
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID);
        }
        Short enable = dto.getEnable();
        if(enable == null ||
                (!WemediaConstants.WM_NEWS_UP.equals(enable)&&!WemediaConstants.WM_NEWS_DOWN.equals(enable))){
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"上下架状态错误");
        }
        //2.查询文章
        WmNews wmNews = getById(dto.getId());
        if(wmNews == null){
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"文章不存在");
        }
        //3.判断文章是否发布
        if(!wmNews.getStatus().equals(WmNews.Status.PUBLISHED.getCode())){
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"当前文章不是发布状态，不能上下架");
        }
        //4.修改文章状态，同步到app端（后期做）TODO
        update(Wrappers.<WmNews>lambdaUpdate().eq(WmNews::getId,dto.getId())
                .set(WmNews::getEnable,dto.getEnable()));

        if(WemediaConstants.WM_NEWS_UP.equals(dto.getEnable())){
            // 上架
            rabbitTemplate.convertAndSend(
                    NewsUpOrDownConstants.NEWS_UP_OR_DOWN_EXCHANGE,
                    NewsUpOrDownConstants.NEWS_UP_ROUTE_KEY,
                    wmNews.getArticleId()
            );
            log.info("成功发送文章 上架消息   文章id: {}",wmNews.getArticleId());
        }else {
            // 下架
            rabbitTemplate.convertAndSend(
                    NewsUpOrDownConstants.NEWS_UP_OR_DOWN_EXCHANGE,
                    NewsUpOrDownConstants.NEWS_DOWN_ROUTE_KEY,
                    wmNews.getArticleId()
            );
            log.info("成功发送文章 下架消息   文章id: {}",wmNews.getArticleId());
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }




    @Autowired
    WmNewsMapper wmNewsMapper;
    /**
     * 查询文章列表
     * @param dto
     * @return
     */
    @Override
    public ResponseResult findList(NewsAuthDTO dto) {
        //1.检查参数
        dto.checkParam();
        //记录当前页
        int currentPage = dto.getPage();
        //设置起始页
        dto.setPage((dto.getPage()-1)*dto.getSize());

        if(StringUtils.isNotBlank(dto.getTitle())){
            //  concat(%,?,%)
            dto.setTitle("%"+dto.getTitle()+"%");
        }

        //2.分页查询
        List<WmNewsVO> wmNewsVoList = wmNewsMapper.findListAndPage(dto);
        //统计多少条数据
        long count = wmNewsMapper.findListCount(dto);

        //3.结果返回
        ResponseResult result = new PageResponseResult(currentPage, dto.getSize(), count, wmNewsVoList);
        result.setHost(webSite);
        return result;
    }


    @Autowired
    WmUserMapper wmUserMapper;

    /**
     * 查询文章详情
     * @param id
     * @return
     */
    @Override
    public ResponseResult findWmNewsVo(Integer id) {
        //1参数检查
        if(id == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2.查询文章信息
        WmNews wmNews = getById(id);
        if(wmNews == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //3.查询作者
        WmUser wmUser = null;
        if(wmNews.getUserId() != null){
            wmUser = wmUserMapper.selectById(wmNews.getUserId());
        }

        //4.封装vo信息返回
        WmNewsVO wmNewsVo = new WmNewsVO();
        BeanUtils.copyProperties(wmNews,wmNewsVo);
        if(wmUser != null){
            wmNewsVo.setAuthorName(wmUser.getName());
        }
        ResponseResult responseResult = ResponseResult.okResult(wmNewsVo);
        responseResult.setHost(webSite);
        return responseResult;
    }




    /**
     * 保存封面 和 素材的关联关系
     * @param urlList
     * @param wmNews
     * @param dto
     */
    private void saveRelativeInfoForCover(List<String> urlList, WmNews wmNews, WmNewsDTO dto) {
        // 1. 获取dto中 传入的封面集合
        List<String> images = dto.getImages();
        // 2. 判断dto中的type属性是否为 -1
        if(WemediaConstants.WM_NEWS_TYPE_AUTO.equals(dto.getType())){
            //     2.1  如果是-1  需要自动生成封面
            //     2.2  根据内容中素材列表生成封面
            int size = urlList.size();
            if(size > 0 && size <=2){
                //         如果内容素材数量大于 0   小于 等于2   生成单图封面
                images = urlList.stream().limit(1).collect(Collectors.toList());
                wmNews.setType(WemediaConstants.WM_NEWS_SINGLE_IMAGE);
            }else if (size > 2) {
                //         如果内容素材数量大于 0   大于  2   生成多图封面
                images = urlList.stream().limit(3).collect(Collectors.toList());
                wmNews.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
            }else {
                //         如果内容素材数量为 0      生成无图封面
                wmNews.setType(WemediaConstants.WM_NEWS_NONE_IMAGE);
            }
            //         重新修改wmNews
            if(!CollectionUtils.isEmpty(images)){
                wmNews.setImages(imagesToStr(images));
            }
            updateById(wmNews);
        }
        // 3. 批量保存 封面 和 素材的关联关系
        if(!CollectionUtils.isEmpty(images)){
            // 如果封面不是自动生成的，是前端直接传过来的 会有前缀路径 记得替换掉
            images = images.stream().map(url -> url.replaceAll(webSite,"")).collect(Collectors.toList());
            saveRelativeInfo(images,wmNews.getId(),WemediaConstants.WM_IMAGE_REFERENCE);
        }
    }

    /**
     * "[{type:text,value:文本},{type:image,value:url路径},{},{}]"
     * @param content
     * @return
     */
    private List<String> parseContentImages(String content) {
        List<Map> contentMapList = JSON.parseArray(content, Map.class);
        return contentMapList.stream()
                        // 筛选 type 为 image 的数据
                        .filter(m -> WemediaConstants.WM_NEWS_TYPE_IMAGE.equals(m.get("type")))
                        // 获取 type为image 这个map对象 中的value的值 得到素材全路径
                        .map(m -> m.get("value").toString())
                        // 替换掉 前缀路径website
                        .map(url -> url.replaceAll(webSite,""))
                        // 去除重复的素材路径
                        .distinct()
                        // 将素材收集到集合中
                        .collect(Collectors.toList());
    }


    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    /**
     * 保存关联关系的核心方法
     * @param urlList  素材路径集合
     * @param newsId   文章id
     * @param type   类型:   0 内容引用      1  封面引用
     */
    public void saveRelativeInfo(List<String> urlList,Integer newsId,Short type){
        // 1. 根据素材路径url集合   查询出对应的素材id列表
//        List<WmMaterial> wmMaterials = wmMaterialMapper.selectList(Wrappers.<WmMaterial>lambdaQuery()
//                .eq(WmMaterial::getUserId, WmThreadLocalUtils.getUser().getId())
//                .in(WmMaterial::getUrl, urlList)
//                .select(WmMaterial::getId)
//        );
        List<Integer> ids = wmMaterialMapper.selectRelationsIds(urlList, WmThreadLocalUtils.getUser().getId());
        // 2. 判断 id列表  长度  是否小于 素材列表长度  如果小于 说明缺失素材
        if(CollectionUtils.isEmpty(ids) || ids.size() < urlList.size()){
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"素材缺失，保存关联关系失败");
        }
        // 3. 根据id列表  newsId  type  保存关联关系 到 wm_news_material
        wmNewsMaterialMapper.saveRelations(ids,newsId,type);
//        for (WmMaterial wmMaterial : wmMaterials) {
//            WmNewsMaterial wmNewsMaterial = new WmNewsMaterial();
//            wmNewsMaterial.setMaterialId(wmMaterial.getId());
//            wmNewsMaterial.setNewsId(newsId);
//            wmNewsMaterial.setType(type);
//            wmNewsMaterialMapper.insert(wmNewsMaterial);
//        }
    }

    @Autowired
    WmNewsMaterialMapper wmNewsMaterialMapper;

    private void saveWmNews(WmNews wmNews) {
        // 1. 补全参数
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        // 上下架
        wmNews.setEnable(WemediaConstants.WM_NEWS_UP);
        // 2. 判断id是否存在  如果不存在 直接保存
        if(wmNews.getId() == null){
            this.save(wmNews);
        }else {
            // 3. 如果存在id  那么先删除之前的关联关系  在 修改
            wmNewsMaterialMapper.delete(Wrappers.<WmNewsMaterial>lambdaQuery().eq(WmNewsMaterial::getNewsId,wmNews.getId()));
            this.updateById(wmNews);
        }
    }

    /**
     * 将图片集合 转为字符串
     *
     * @param images
     * @return
     */
    private String imagesToStr(List<String> images) {
        return images.stream().map(url-> url.replaceAll(webSite,""))
                        .collect(Collectors.joining(","));
    }


    /**
     * 自媒体文章人工审核
     * @param status 2  审核失败  4 审核成功
     * @param dto
     * @return
     */
    @Override
    public ResponseResult updateStatus(Short status, NewsAuthDTO dto) {
        //1.参数检查
        if(dto == null || dto.getId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        //2.查询文章
        WmNews wmNews = getById(dto.getId());
        if(wmNews == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        // 检查文章状态 不能为9  已发布
        if (wmNews.getStatus().equals(WmNews.Status.PUBLISHED.getCode())) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_ALLOW,"文章已发布");
        }
        //3.修改文章状态
        wmNews.setStatus(status);
        if(StringUtils.isNotBlank(dto.getMsg())){
            wmNews.setReason(dto.getMsg());
        }
        updateById(wmNews);
        // 通知定时发布文章
        if (status.shortValue() == WmNews.Status.ADMIN_SUCCESS.getCode()) {
            // 获取发布时间
            long publishTime = wmNews.getPublishTime().getTime();
            // 获取当前时间
            long nowTime = System.currentTimeMillis();
            // 发布时间 - 当前时间 = 距离发布的延迟时间
            long remainTime = publishTime - nowTime;
            // 使用rabbittemplate 发送延迟消息
            rabbitTemplate.convertAndSend(
                    PublishArticleConstants.DELAY_DIRECT_EXCHANGE,
                    PublishArticleConstants.PUBLISH_ARTICLE_ROUTE_KEY,
                    wmNews.getId(),
                    message -> {
                        message.getMessageProperties().setHeader("x-delay",remainTime<=0?0:remainTime);
                        return message;
                    }
            );
            log.info("成功发送  文章延迟发布消息   文章id: {}    当前时间: {}",wmNews.getId(), LocalDateTime.now());
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
