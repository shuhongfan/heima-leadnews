package com.heima.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.HotArticleService;
import com.heima.common.constants.article.ArticleConstants;
import com.heima.common.exception.CustException;
import com.heima.feigns.AdminFeign;
import com.heima.model.admin.pojo.AdChannel;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.vos.HotArticleVo;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.message.app.AggBehaviorDTO;
import com.heima.utils.common.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class HotArticleServiceImpl implements HotArticleService {

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private AdminFeign adminFeign;

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 计算热文章
     */
    @Override
    public void computeHotArticle() {
//        1.查询近5天的文章
//        1.1 计算5天前的时间
        String param =  LocalDateTime.now().minusDays(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));

//        1.2 使用articleMapper查询文章数据
        List<ApArticle> articleList = apArticleMapper.selectArticleByDate(param);
        if (CollectionUtils.isNotEmpty(articleList)) {
            log.info("当前头条项目太冷清，近5天没人发布文章了");
            return;
        }

//        2. 计算每一篇文章热度得分
        List<HotArticleVo> articleVoList = getHotArticleVoList(articleList);

//        3. 按照频道 每个频道缓存 热度最高的30条文章
        cacheToRedisByTag(articleVoList);

    }

    /**
     * 重新计算文章热度  更新redis缓存
     * @param aggBehavior
     */
    @Override
    public void updateApArticle(AggBehaviorDTO aggBehavior) {
//        1. 根据id 查询文章数据
        ApArticle apArticle = apArticleMapper.selectById(aggBehavior.getArticleId());
        if (apArticle == null) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST, "对应的文章数据不存在");
        }

//        2. 将聚合文章行为文章数据 和  文章表中的统计数据 累加到一起
        Integer views = apArticle.getViews() == null ? 0 : apArticle.getViews();
        apArticle.setViews((int) (views+ aggBehavior.getView()));

        Integer likes = apArticle.getLikes() == null ? 0 : apArticle.getLikes();
        apArticle.setLikes((int) (likes+ aggBehavior.getLike()));

        Integer collections = apArticle.getCollection() == null ? 0 : apArticle.getCollection();
        apArticle.setCollection((int) (collections+ aggBehavior.getCollect()));

        Integer comments = apArticle.getComment() == null ? 0 : apArticle.getComment();
        apArticle.setComment((int) (comments+ aggBehavior.getComment()));

        apArticleMapper.updateById(apArticle);

//        3. 重新计算文章热度得分
        Integer score = computeScore(apArticle);

//        4.如果文章是今天发布的  热度*3
//        当前时间
        String now = DateUtils.dateToString(new Date());
//        发布时间
        String publishTime = DateUtils.dateToString(apArticle.getPublishTime());
        if (publishTime.equals(now)) {
            score = score * 3;
        }

//        5. 更新当前文章所在频道的缓存
        updateApArticleCache(apArticle,score,ArticleConstants.HOT_ARTICLE_FIRST_PAGE+apArticle.getChannelId());

//        6. 更新推进频道的缓存
        updateApArticleCache(apArticle, score, ArticleConstants.HOT_ARTICLE_FIRST_PAGE + ArticleConstants.DEFAULT_TAG);

    }

    /**
     * 5. 更新当前文章所在频道的缓存
     * @param article
     * @param score
     * @param cacheKey
     */
    private void updateApArticleCache(ApArticle article, Integer score, String cacheKey) {
//        1. 从redis中 查询出 对应的热点文章列表
        String hotArticleJSON = redisTemplate.opsForValue().get(cacheKey);
        List<HotArticleVo> articleVoList = JSON.parseArray(hotArticleJSON, HotArticleVo.class);

//        2. 判断当前文章是否存在热点文章列表中
        boolean isHas = false;
        for (HotArticleVo hotArticleVo : articleVoList) {
            if (hotArticleVo.getId().equals(article.getId())) {
//        3. 如果存在 直接更新文章score热度值
                hotArticleVo.setScore(score);
                isHas = true;
                break;
            }
        }

//        4. 如果不存在  直接将当前文章 加入到热点文章列表
        if (!isHas) {
            HotArticleVo articleVo = new HotArticleVo();
            BeanUtils.copyProperties(article, articleVo);
            articleVo.setScore(score);
            articleVoList.add(articleVo);
        }

//        5. 重新将热点文章列表 按照热度降序排序 截取前30条文章
        articleVoList = articleVoList.stream()
                .sorted(Comparator.comparing(HotArticleVo::getScore).reversed())
                .limit(30)
                .collect(Collectors.toList());

//        6. 将文章集合 重新 存入redis中
        redisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(articleVoList));
    }

    /**
     * 按照频道 每个频道缓存 热度最高的30条文章
     * @param articleVoList
     */
    private void cacheToRedisByTag(List<HotArticleVo> articleVoList) {
//        1. 远程查询评论列表
        ResponseResult<List<AdChannel>> result = adminFeign.selectChannels();
        if (!result.checkCode()) {
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR, result.getErrorMessage());
        }
        List<AdChannel> channelList = result.getData();

//        2. 遍历频道列表，从文章列表中筛选每个频道对应的文章 调用保存方法 sortAndCache
        for (AdChannel channel : channelList) {
            List<HotArticleVo> hotArticleVoList = articleVoList.stream()
                    .filter(articleVo -> articleVo.getChannelId().equals(channel.getId()))
                    .collect(Collectors.toList());
            sortAndCache(hotArticleVoList, ArticleConstants.HOT_ARTICLE_FIRST_PAGE + channel.getId());
        }
//        3. 缓存推荐频道的文章 调用保存方法 sortAndCache
        sortAndCache(articleVoList, ArticleConstants.HOT_ARTICLE_FIRST_PAGE + ArticleConstants.DEFAULT_TAG);
    }

    /**
     * 3. 缓存推荐频道的文章 调用保存方法 sortAndCache
     * @param hotArticleVoList
     * @param cacheKey
     */
    private void sortAndCache(List<HotArticleVo> hotArticleVoList, String cacheKey) {
//        1. 按照热度降序排序 截取前30条文章
        hotArticleVoList = hotArticleVoList.stream()
                .sorted(Comparator.comparing(HotArticleVo::getScore).reversed())
                .limit(30)
                .collect(Collectors.toList());

//        2. 缓存到redis中
        redisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(hotArticleVoList), 24, TimeUnit.HOURS);
    }

    /**
     * 计算每一篇文章热度得分
     * @param articleList
     * @return
     */
    private List<HotArticleVo> getHotArticleVoList(List<ApArticle> articleList) {
//        将每个文章计算得分，封装成vo对象
        return articleList.stream().map(apArticle -> {
            HotArticleVo articleVo = new HotArticleVo();
            BeanUtils.copyProperties(apArticle, articleVo);

//            计算得分
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
        if (views != null) {
            score += views * ArticleConstants.HOT_ARTICLE_VIEW_WEIGHT;
        }

        Integer likes = apArticle.getLikes();
        if (likes != null) {
            score += likes * ArticleConstants.HOT_ARTICLE_LIKE_WEIGHT;
        }

        Integer comment = apArticle.getComment();
        if (comment != null) {
            score += comment * ArticleConstants.HOT_ARTICLE_COMMENT_WEIGHT;
        }

        Integer collection = apArticle.getCollection();
        if (collection != null) {
            score += collection * ArticleConstants.HOT_ARTICLE_COLLECTION_WEIGHT;
        }

        return score;
    }
}
