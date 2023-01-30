package com.heima.model.article.vos;
import com.heima.model.common.ano.EsId;
import lombok.Data;
import java.util.Date;
@Data
public class SearchArticleVO {
    // 文章id
    @EsId
    private Long id;
    // 文章标题
    private String title;
    // 文章发布时间
    private Date publishTime;
    // 文章布局
    private Integer layout;
    // 封面
    private String images;
    // 作者
    private Long authorId;
    // 作者名字
    private String authorName;
    //静态url
    private String staticUrl;
}