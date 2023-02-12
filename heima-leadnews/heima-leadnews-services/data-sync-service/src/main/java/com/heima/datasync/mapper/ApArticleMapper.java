package com.heima.datasync.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.article.pojos.ApArticle;

import java.util.List;

public interface ApArticleMapper extends BaseMapper<ApArticle> {
    List<ApArticle> findAllArticles();
}
