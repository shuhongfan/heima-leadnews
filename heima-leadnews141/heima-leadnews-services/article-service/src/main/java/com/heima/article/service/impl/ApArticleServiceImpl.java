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
        // 1. 根据id查询并校验自媒体文章
        WmNews wmNews = getWmNews(newsId);
        // 2. 根据wmNews 封装 apArticle对象
        ApArticle article = getApArticle(wmNews);
        // 3. 保存或修改 apArticle
        saveOrUpdateArticle(article);
        // 4. 保存关联的 配置 和 内容信息
        saveConfigAndContent(wmNews,article);
        // 5. 基于新的文章内容生成 html静态页
        generatePageService.generateArticlePage(wmNews.getContent(),article);
        // 6. 更新wmNews状态 9
        updateWmNews(wmNews,article);
        // 7. 通过es 更新索引库
        rabbitTemplate.convertAndSend(NewsUpOrDownConstants.NEWS_UP_FOR_ES_QUEUE,article.getId());
        log.info("文章 发布成功，并通知 search搜索微服务   更新索引库所长信息 ================ {}",article.getId());
    }


    @Autowired
    ApArticleMapper apArticleMapper;

    @Value("${file.oss.web-site}")
    String webSite;
    @Value("${file.minio.readPath}")
    String readPath;
    @Override
    public ResponseResult load(Short loadtype, ArticleHomeDTO dto) {
        // 1. 检查参数: (分页  时间  类型  频道)
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
        // 2. 调用mapper查询
        List<ApArticle> articleList = apArticleMapper.loadArticleList(dto, loadtype);
        // 3.  返还结果   (封面需要拼接访问前缀  )
        for (ApArticle article : articleList) {
            // url,url2,url3 ==>  website+url,website+url2，website+url3
            parseArticle(article);
        }
        return ResponseResult.okResult(articleList);
    }
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public ResponseResult load2(Short loadtype, ArticleHomeDTO dto, boolean firstPage) {
        if(firstPage){
            // 1. 从redis缓存中 查询热点文章数据
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
        // url,url2,url3 ==>  website+url,website+url2，website+url3
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
        // 将文章改为发布状态
        wmNews.setStatus(WmNews.Status.PUBLISHED.getCode());
        // 关联articleId
        wmNews.setArticleId(article.getId());
        ResponseResult result = wemediaFeign.updateWmNews(wmNews);
        if (!result.checkCode()) {
            log.error("远程调用修改自媒体文章接口失败  {}",result.getErrorMessage());
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR,result.getErrorMessage());
        }
    }

    private void saveConfigAndContent(WmNews wmNews, ApArticle article) {
        // 1. 保存配置
        ApArticleConfig config = new ApArticleConfig();
        config.setArticleId(article.getId());
        // 是否允许评论
        config.setIsComment(true);
        // 是否允许转发
        config.setIsForward(true);
        // 是否 下架
        config.setIsDown(false);
        // 是否 删除
        config.setIsDelete(false);
        apArticleConfigMapper.insert(config);
        // 2. 保存文章详情
        ApArticleContent content = new ApArticleContent();
        content.setArticleId(article.getId());
        content.setContent(wmNews.getContent());
        apArticleContentMapper.insert(content);
    }

    /**
     * 保存或修改文章
     * @param article
     */
    private void saveOrUpdateArticle(ApArticle article) {
        // 1. 判断文章id是否存在
        if(article.getId() == null){
            // 2 如果不存在  保存文章
            article.setLikes(0);
            article.setViews(0);
            article.setComment(0);
            article.setCollection(0);
            save(article);
        }else {
            // 3 如果存在    修改文章
            //     3.1 查询 之前的article是否存在
            ApArticle oldArticle = this.getById(article.getId());
            if(oldArticle == null){
                CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"关联的文章数据不存在");
            }
            //     3.2 修改文章
            updateById(article);
            //     3.3 删除之前所关联的  config  和  content
            apArticleConfigMapper.delete(Wrappers.<ApArticleConfig>lambdaQuery().eq(ApArticleConfig::getArticleId,article.getId()));
            apArticleContentMapper.delete(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId,article.getId()));
        }
    }

    private ApArticle getApArticle(WmNews wmNews) {
        //1. 创建article对象
        ApArticle article = new ApArticle();
        //2. 拷贝属性  wmNews ==> article
        BeanUtils.copyProperties(wmNews,article);
        //3. 补全其它属性   id   flag    layout
        article.setId(wmNews.getArticleId());
        article.setFlag((byte)0);
        article.setLayout(wmNews.getType());
        //4. 补全频道信息  channel_id  远程 去 ad_channel查
        ResponseResult<AdChannel> channelResult = adminFeign.findOne(wmNews.getChannelId());
        if(!channelResult.checkCode()){
            log.error("远程调用查询频道接口失败  {}",channelResult.getErrorMessage());
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR,channelResult.getErrorMessage());
        }
        AdChannel channel = channelResult.getData();
        if (channel == null) {
            log.error("远程查询频道信息为null    频道id:  {}",wmNews.getChannelId());
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"频道不存在");
        }
        article.setChannelName(channel.getName());
        //5. 补全作者信息  wm_user_id  去 ap_author 查
        ApAuthor apAuthor = authorMapper.selectOne(Wrappers.<ApAuthor>lambdaQuery().eq(ApAuthor::getWmUserId, wmNews.getUserId()));
        if (apAuthor == null) {
            log.error("关联的作者信息不存在    wmUserId:  {}",wmNews.getUserId());
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"关联的作者信息不存在");
        }
        article.setAuthorId(Long.valueOf(apAuthor.getId()));
        article.setAuthorName(apAuthor.getName());
        return article;
    }


    @Autowired
    WemediaFeign wemediaFeign;

    /**
     * 获取自媒体文章
     * @param newsId
     * @return
     */
    private WmNews getWmNews(Integer newsId) {
        //1. 根据id远程查询自媒体文章
        ResponseResult<WmNews> result = wemediaFeign.findWmNewsById(newsId);
        //2. 检查远程调用状态
        if (!result.checkCode()) {
            log.error("远程调用自媒体文章信息失败  文章id: {}",newsId);
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR,result.getErrorMessage());
        }
        //3. 判断news是否为null
        WmNews wmNews = result.getData();
        if (wmNews == null) {
            log.error("对应的文章信息 不存在 文章id: "+newsId);
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"对应的文章信息 不存在 文章id: "+newsId);
        }
        //4. 检查news状态是否为  4  或  8
        short status = wmNews.getStatus().shortValue();
        if(WmNews.Status.ADMIN_SUCCESS.getCode()!=status && WmNews.Status.SUCCESS.getCode()!=status){
            log.error("对应的文章状态  不为4 或 8   不予发布   文章id: {}",newsId);
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"对应的文章信息 不存在 文章id: "+newsId);
        }
        return wmNews;
    }
}