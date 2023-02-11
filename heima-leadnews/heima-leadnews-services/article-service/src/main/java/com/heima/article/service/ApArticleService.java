package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.article.dtos.ArticleHomeDTO;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.common.dtos.ResponseResult;

public interface ApArticleService extends IService<ApArticle> {
    /**
     * 保存或修改文章
     * @param newsId 文章id
     * @return
     */
    public void publishArticle(Integer newsId);

    /**
     * 根据参数加载文章列表
     * @param loadtype 0为加载更多  1为加载最新
     * @param dto
     * @return
     */
    ResponseResult load(Short loadtype, ArticleHomeDTO dto);

    /**
     * 根据参数加载文章列表  v2
     * @param loadtypeLoadMore
     * @param dto
     * @param firstPage
     * @return
     */
    public ResponseResult load2(Short loadtypeLoadMore, ArticleHomeDTO dto,boolean firstPage);
}
