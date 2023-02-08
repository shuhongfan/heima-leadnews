package com.heima.model.behavior.pojos;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Data
@Document("ap_likes_behavior")
public class ApLikesBehavior implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    /**
     * 实体ID
     */
    private String entryId;
    // 文章id
    private Long articleId;
    /**
     * 点赞内容类型
     * 0文章
     * 1动态
     */
    private Short type;
    /**
     * 0 点赞
     * 1 取消点赞
     */
    private Short operation;
    /**
     * 创建时间
     */
    private Date createdTime;
}