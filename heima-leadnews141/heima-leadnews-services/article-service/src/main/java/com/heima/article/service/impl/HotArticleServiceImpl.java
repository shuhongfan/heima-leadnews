package com.heima.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.HotArticleService;
import com.heima.common.exception.CustException;
import com.heima.feigns.AdminFeign;
import com.heima.model.admin.pojo.AdChannel;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.vos.HotArticleVo;
import com.heima.model.common.constants.article.ArticleConstants;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.mess.app.AggBehaviorDTO;
import com.heima.utils.common.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author mrchen
 * @date 2022/5/6 9:55
 */
@Service
@Slf4j
public class HotArticleServiceImpl implements HotArticleService {

    @Autowired
    ApArticleMapper apArticleMapper;

    @Override
    public void computeHotArticle() {
        // 1. 查询近5天的文章
        // 1.1 计算5天前的 时间
        String param = LocalDateTime.now().minusDays(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // 1.2 使用articleMapper查询文章数据
        List<ApArticle> articleList = apArticleMapper.selectArticleByDate(param);
        if(CollectionUtils.isEmpty(articleList)){
            log.info(" 当前头条项目太冷清了，近5天没人发布文章了~~~~");
            return;
        }
        // 2. 计算每一篇文章热度得分
        List<HotArticleVo> articleVoList = getHotArticleVoList(articleList);

        // 3. 按照频道 每个频道缓存 热度最高的30条文章
        cacheToRedisByTag(articleVoList);
    }

    @Override
    public void updateApArticle(AggBehaviorDTO aggBehavior) {
        // 1. 根据id 查询文章数据
        ApArticle article = apArticleMapper.selectById(aggBehavior.getArticleId());
        if (article == null) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"对应的文章数据不存在");
        }
        // 2. 将聚合行为文章数据  和  文章表中的统计数据  累加一起
        // 更新阅读量
        Integer views = article.getViews()==null?0:article.getViews();
        article.setViews((int)(views + aggBehavior.getView()));
        // 更新点赞量
        Integer likes = article.getLikes()==null?0:article.getLikes();
        article.setLikes((int)(likes + aggBehavior.getLike()));
        // 更新评论量
        Integer comment = article.getComment()==null?0:article.getComment();
        article.setComment((int)(comment + aggBehavior.getComment()));
        // 更新收藏量
        Integer collection = article.getCollection()==null?0:article.getCollection();
        article.setCollection((int)(collection + aggBehavior.getCollect()));
        apArticleMapper.updateById(article);
        // 3. 重新计算文章热度得分
        Integer score = computeScore(article);
        // 4. 如果文章是今天发布的  热度 * 3
        // 当前时间
        String now = DateUtils.dateToString(new Date());
        // 发布时间
        String publishTime = DateUtils.dateToString(article.getPublishTime());
        if(publishTime.equals(now)){
            score = score*3;
        }
        // 5.  更新当前文章所在频道的缓存
        updateArticleCache(article, score, ArticleConstants.HOT_ARTICLE_FIRST_PAGE + article.getChannelId());
        // 6.  更新推荐频道的缓存
        updateArticleCache(article, score, ArticleConstants.HOT_ARTICLE_FIRST_PAGE + ArticleConstants.DEFAULT_TAG);
    }



    private void updateArticleCache(ApArticle article, Integer score, String cacheKey) {
        // 1. 从redis中 查询出 对应的热点文章列表
        String hotArticleJson = redisTemplate.opsForValue().get(cacheKey);
        List<HotArticleVo> hotArticleVoList = JSON.parseArray(hotArticleJson, HotArticleVo.class);
        // 2. 判断当前文章是否 存在热点文章列表中
        boolean isHas = false;
        for (HotArticleVo articleVo : hotArticleVoList) {
            // 3.    如果存在  直接更新文章 score热度值
            if(articleVo.getId().equals(article.getId())){
                // 当前文章已经存在热点文章中，更新得分即可
                articleVo.setScore(score);
                isHas = true;
                break;
            }
        }
        // 4.    如果不存在  直接将当前文章 加入到热点文章列表
        if(!isHas){
            HotArticleVo articleVo = new HotArticleVo();
            BeanUtils.copyProperties(article,articleVo);
            articleVo.setScore(score);
            hotArticleVoList.add(articleVo);
        }
        // 5.  重新将热点文章列表 按照热度降序排序   截取前30条文章
        hotArticleVoList = hotArticleVoList.stream()
                                            .sorted(Comparator.comparing(HotArticleVo::getScore).reversed())
                                            .limit(30)
                                            .collect(Collectors.toList());
        // 6.   将文章集合 重新 存入到redis中
        redisTemplate.opsForValue().set(cacheKey,JSON.toJSONString(hotArticleVoList));
    }

    @Autowired
    AdminFeign adminFeign;


    private void cacheToRedisByTag(List<HotArticleVo> articleVoList) {
        //1.  远程查询频道列表
        ResponseResult<List<AdChannel>> result = adminFeign.selectChannels();
        if (!result.checkCode()) {
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR,result.getErrorMessage());
        }
        List<AdChannel> channelList = result.getData();
        //2.  遍历频道列表， 从文章列表中筛选每个频道对应的文章集合  调用保存方法  sortAndCache
        for (AdChannel channel : channelList) {
            List<HotArticleVo> hotArticlesByTag = articleVoList.stream()
                    .filter(articleVo -> articleVo.getChannelId().equals(channel.getId()))
                    .collect(Collectors.toList());
            sortAndCache(hotArticlesByTag,ArticleConstants.HOT_ARTICLE_FIRST_PAGE + channel.getId());
        }
        //3.  缓存推荐频道的文章  调用保存方法  sortAndCache
        sortAndCache(articleVoList,ArticleConstants.HOT_ARTICLE_FIRST_PAGE + ArticleConstants.DEFAULT_TAG);
    }


    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * @param hotArticleList  待缓存的热点文章
     * @param cacheKey   redis中的key
     */
    private void sortAndCache(List<HotArticleVo> hotArticleList, String cacheKey) {
        // 1. 按照热度降序排序 截取前30条文章
        hotArticleList = hotArticleList.stream()
                .sorted(Comparator.comparing(HotArticleVo::getScore).reversed())
                .limit(30)
                .collect(Collectors.toList());
        // 2. 缓存到redis中
        redisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(hotArticleList));
    }
    /**
     * 计算每一篇文章的热度得分
     * @param articleList
     * @return
     */
    private List<HotArticleVo> getHotArticleVoList(List<ApArticle> articleList) {
                // 遍历文章
        return articleList.stream()
                    // 将每个文章计算得分 ，封装成vo对象
                    .map(apArticle -> {
                        HotArticleVo articleVo = new HotArticleVo();
                        BeanUtils.copyProperties(apArticle,articleVo);
                        // 计算文章得分
                        Integer score = computeScore(apArticle);
                        articleVo.setScore(score);
                        return articleVo;
                    }).collect(Collectors.toList());
    }

    /**
     * 计算文章热度得分
     * @param apArticle
     * @return
     */
    private Integer computeScore(ApArticle apArticle) {
        Integer score = 0;
        Integer views = apArticle.getViews();
        if(views !=null ){
            score += views * ArticleConstants.HOT_ARTICLE_VIEW_WEIGHT;
        }
        Integer likes = apArticle.getLikes();
        if(likes !=null ){
            score += likes * ArticleConstants.HOT_ARTICLE_LIKE_WEIGHT;
        }
        Integer comment = apArticle.getComment();
        if(comment !=null ){
            score += comment * ArticleConstants.HOT_ARTICLE_COMMENT_WEIGHT;
        }
        Integer collection = apArticle.getCollection();
        if(collection !=null ){
            score += collection * ArticleConstants.HOT_ARTICLE_COLLECTION_WEIGHT;
        }
        return score;
    }
}
