package com.heima.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.article.dtos.ArticleHomeDTO;
import com.heima.model.article.pojos.ApArticle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApArticleMapper extends BaseMapper<ApArticle> {

    /**
     * 查询文章列表
     * @param dto
     * @param type 0：加载更多   1：加载最新
     * @return
     */
    public List<ApArticle> loadArticleList(@Param("dto") ArticleHomeDTO dto, @Param("type") Short type);
}
