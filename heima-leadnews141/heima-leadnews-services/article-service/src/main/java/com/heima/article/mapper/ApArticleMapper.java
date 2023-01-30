package com.heima.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.article.dtos.ArticleHomeDTO;
import com.heima.model.article.pojos.ApArticle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ApArticleMapper extends BaseMapper<ApArticle> {
    @Select("select aa.* from ap_article aa left join ap_article_config aac on aa.id=aac.article_id " +
            "where aac.is_delete!=1 and aac.is_down != 1 " +
            "and aa.publish_time > #{beginDate}")
    public List<ApArticle> selectArticleByDate(@Param("beginDate") String beginDate);
    /**
     * 查询文章列表
     * @param dto
     * @param type 0：加载更多   1：加载最新
     * @return
     */
    public List<ApArticle> loadArticleList(@Param("dto") ArticleHomeDTO dto, @Param("type") Short type);
}