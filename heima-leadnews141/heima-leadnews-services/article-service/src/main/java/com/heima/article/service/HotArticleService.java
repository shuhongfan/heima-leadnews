package com.heima.article.service;

import com.heima.model.mess.app.AggBehaviorDTO;

/**
 * <p>
 * 热文章表 服务类
 * </p>
 *
 * @author itheima
 */
public interface HotArticleService{
    /**
     * 计算热文章
     */
    public void computeHotArticle();
    /**
     * 重新计算文章热度  更新redis缓存
     * @param aggBehavior
     */
    public void updateApArticle(AggBehaviorDTO aggBehavior);
}