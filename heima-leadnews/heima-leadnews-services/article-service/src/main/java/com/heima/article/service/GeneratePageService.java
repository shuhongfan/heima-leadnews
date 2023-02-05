package com.heima.article.service;

import com.heima.model.article.pojos.ApArticle;

public interface GeneratePageService {
    /**
     * 生成文章静态页
     */
    void generateArticlePage(String content, ApArticle apArticle);
}
