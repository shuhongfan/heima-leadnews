package com.heima.datasync.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.article.pojos.ApArticle;
import org.apache.ibatis.annotations.Select;
import java.util.List;
public interface ApArticleMapper extends BaseMapper<ApArticle> {
    @Select("select aa.* from ap_article aa left join ap_article_config" +
            " aac on aa.id = aac.article_id where aac.is_delete !=1 and aac.is_down !=1")
    List<ApArticle> findAllArticles();
}