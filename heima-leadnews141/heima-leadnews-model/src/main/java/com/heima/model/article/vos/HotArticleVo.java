package com.heima.model.article.vos;

import com.heima.model.article.pojos.ApArticle;
import lombok.Data;
@Data
public class HotArticleVo extends ApArticle {
    /**
     * 分值
     */
    private Integer score;
}