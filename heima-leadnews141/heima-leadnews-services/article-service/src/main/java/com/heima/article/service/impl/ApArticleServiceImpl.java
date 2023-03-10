package com.heima.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.mapper.AuthorMapper;
import com.heima.article.service.ApArticleService;
import com.heima.article.service.GeneratePageService;
import com.heima.common.exception.CustException;
import com.heima.feigns.AdminFeign;
import com.heima.feigns.WemediaFeign;
import com.heima.model.admin.pojo.AdChannel;
import com.heima.model.article.dtos.ArticleHomeDTO;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.article.pojos.ApArticleContent;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.constants.article.ArticleConstants;
import com.heima.model.common.constants.article.HotArticleConstants;
import com.heima.model.common.constants.message.NewsUpOrDownConstants;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.pojos.WmNews;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {
    @Autowired
    AdminFeign adminFeign;
    @Autowired
    AuthorMapper authorMapper;
    @Autowired
    ApArticleConfigMapper apArticleConfigMapper;

    @Autowired
    GeneratePageService generatePageService;

    @Autowired
    ApArticleContentMapper apArticleContentMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @GlobalTransactional(rollbackFor = Exception.class,timeoutMills = 300000)
    @Override
    public void publishArticle(Integer newsId) {
        // 1. ??????id??????????????????????????????
        WmNews wmNews = getWmNews(newsId);
        // 2. ??????wmNews ?????? apArticle??????
        ApArticle article = getApArticle(wmNews);
        // 3. ??????????????? apArticle
        saveOrUpdateArticle(article);
        // 4. ??????????????? ?????? ??? ????????????
        saveConfigAndContent(wmNews,article);
        // 5. ?????????????????????????????? html?????????
        generatePageService.generateArticlePage(wmNews.getContent(),article);
        // 6. ??????wmNews?????? 9
        updateWmNews(wmNews,article);
        // 7. ??????es ???????????????
        rabbitTemplate.convertAndSend(NewsUpOrDownConstants.NEWS_UP_FOR_ES_QUEUE,article.getId());
        log.info("?????? ???????????????????????? search???????????????   ??????????????????????????? ================ {}",article.getId());
    }


    @Autowired
    ApArticleMapper apArticleMapper;

    @Value("${file.oss.web-site}")
    String webSite;
    @Value("${file.minio.readPath}")
    String readPath;
    @Override
    public ResponseResult load(Short loadtype, ArticleHomeDTO dto) {
        // 1. ????????????: (??????  ??????  ??????  ??????)
        Integer size = dto.getSize();
        if(size==null || size<=0){
            dto.setSize(10);
        }
        if(dto.getMinBehotTime() == null){
            dto.setMinBehotTime(new Date());
        }
        if(dto.getMaxBehotTime() == null){
            dto.setMaxBehotTime(new Date());
        }
        if (StringUtils.isBlank(dto.getTag())) {
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }
        if(!loadtype.equals(ArticleConstants.LOADTYPE_LOAD_NEW)&&!loadtype.equals(ArticleConstants.LOADTYPE_LOAD_MORE)){
            loadtype = ArticleConstants.LOADTYPE_LOAD_MORE;
        }
        // 2. ??????mapper??????
        List<ApArticle> articleList = apArticleMapper.loadArticleList(dto, loadtype);
        // 3.  ????????????   (??????????????????????????????  )
        for (ApArticle article : articleList) {
            // url,url2,url3 ==>  website+url,website+url2???website+url3
            parseArticle(article);
        }
        return ResponseResult.okResult(articleList);
    }
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public ResponseResult load2(Short loadtype, ArticleHomeDTO dto, boolean firstPage) {
        if(firstPage){
            // 1. ???redis????????? ????????????????????????
            String articleListJson = redisTemplate.opsForValue().get(ArticleConstants.HOT_ARTICLE_FIRST_PAGE + dto.getTag());
            if (StringUtils.isNotBlank(articleListJson)) {
                List<ApArticle> articleList = JSON.parseArray(articleListJson, ApArticle.class);
                for (ApArticle article : articleList) {
                    parseArticle(article);
                }
                return ResponseResult.okResult(articleList);
            }
        }
        return load(loadtype,dto);
    }

    private void parseArticle(ApArticle article) {
        // url,url2,url3 ==>  website+url,website+url2???website+url3
        String images = article.getImages();
        if (StringUtils.isNotBlank(images)) {
            images = Arrays.stream(images.split(","))
                    .map(url -> webSite + url)
                    .collect(Collectors.joining(","));
            article.setImages(images);
        }

        article.setStaticUrl(readPath + article.getStaticUrl());
    }

    private void updateWmNews(WmNews wmNews, ApArticle article) {
        // ???????????????????????????
        wmNews.setStatus(WmNews.Status.PUBLISHED.getCode());
        // ??????articleId
        wmNews.setArticleId(article.getId());
        ResponseResult result = wemediaFeign.updateWmNews(wmNews);
        if (!result.checkCode()) {
            log.error("?????????????????????????????????????????????  {}",result.getErrorMessage());
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR,result.getErrorMessage());
        }
    }

    private void saveConfigAndContent(WmNews wmNews, ApArticle article) {
        // 1. ????????????
        ApArticleConfig config = new ApArticleConfig();
        config.setArticleId(article.getId());
        // ??????????????????
        config.setIsComment(true);
        // ??????????????????
        config.setIsForward(true);
        // ?????? ??????
        config.setIsDown(false);
        // ?????? ??????
        config.setIsDelete(false);
        apArticleConfigMapper.insert(config);
        // 2. ??????????????????
        ApArticleContent content = new ApArticleContent();
        content.setArticleId(article.getId());
        content.setContent(wmNews.getContent());
        apArticleContentMapper.insert(content);
    }

    /**
     * ?????????????????????
     * @param article
     */
    private void saveOrUpdateArticle(ApArticle article) {
        // 1. ????????????id????????????
        if(article.getId() == null){
            // 2 ???????????????  ????????????
            article.setLikes(0);
            article.setViews(0);
            article.setComment(0);
            article.setCollection(0);
            save(article);
        }else {
            // 3 ????????????    ????????????
            //     3.1 ?????? ?????????article????????????
            ApArticle oldArticle = this.getById(article.getId());
            if(oldArticle == null){
                CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"??????????????????????????????");
            }
            //     3.2 ????????????
            updateById(article);
            //     3.3 ????????????????????????  config  ???  content
            apArticleConfigMapper.delete(Wrappers.<ApArticleConfig>lambdaQuery().eq(ApArticleConfig::getArticleId,article.getId()));
            apArticleContentMapper.delete(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId,article.getId()));
        }
    }

    private ApArticle getApArticle(WmNews wmNews) {
        //1. ??????article??????
        ApArticle article = new ApArticle();
        //2. ????????????  wmNews ==> article
        BeanUtils.copyProperties(wmNews,article);
        //3. ??????????????????   id   flag    layout
        article.setId(wmNews.getArticleId());
        article.setFlag((byte)0);
        article.setLayout(wmNews.getType());
        //4. ??????????????????  channel_id  ?????? ??? ad_channel???
        ResponseResult<AdChannel> channelResult = adminFeign.findOne(wmNews.getChannelId());
        if(!channelResult.checkCode()){
            log.error("????????????????????????????????????  {}",channelResult.getErrorMessage());
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR,channelResult.getErrorMessage());
        }
        AdChannel channel = channelResult.getData();
        if (channel == null) {
            log.error("???????????????????????????null    ??????id:  {}",wmNews.getChannelId());
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"???????????????");
        }
        article.setChannelName(channel.getName());
        //5. ??????????????????  wm_user_id  ??? ap_author ???
        ApAuthor apAuthor = authorMapper.selectOne(Wrappers.<ApAuthor>lambdaQuery().eq(ApAuthor::getWmUserId, wmNews.getUserId()));
        if (apAuthor == null) {
            log.error("??????????????????????????????    wmUserId:  {}",wmNews.getUserId());
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"??????????????????????????????");
        }
        article.setAuthorId(Long.valueOf(apAuthor.getId()));
        article.setAuthorName(apAuthor.getName());
        return article;
    }


    @Autowired
    WemediaFeign wemediaFeign;

    /**
     * ?????????????????????
     * @param newsId
     * @return
     */
    private WmNews getWmNews(Integer newsId) {
        //1. ??????id???????????????????????????
        ResponseResult<WmNews> result = wemediaFeign.findWmNewsById(newsId);
        //2. ????????????????????????
        if (!result.checkCode()) {
            log.error("???????????????????????????????????????  ??????id: {}",newsId);
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR,result.getErrorMessage());
        }
        //3. ??????news?????????null
        WmNews wmNews = result.getData();
        if (wmNews == null) {
            log.error("????????????????????? ????????? ??????id: "+newsId);
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"????????????????????? ????????? ??????id: "+newsId);
        }
        //4. ??????news???????????????  4  ???  8
        short status = wmNews.getStatus().shortValue();
        if(WmNews.Status.ADMIN_SUCCESS.getCode()!=status && WmNews.Status.SUCCESS.getCode()!=status){
            log.error("?????????????????????  ??????4 ??? 8   ????????????   ??????id: {}",newsId);
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"????????????????????? ????????? ??????id: "+newsId);
        }
        return wmNews;
    }
}