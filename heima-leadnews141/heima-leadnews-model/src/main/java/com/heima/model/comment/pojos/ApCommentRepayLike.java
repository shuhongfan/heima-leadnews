package com.heima.model.comment.pojos;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * APP评论回复信息点赞信息
 */
@Data
@Document("ap_comment_repay_like")
public class ApCommentRepayLike {

    /**
     * id
     */
    private String id;

    /**
     * 用户ID
     */
    private Integer authorId;

    /**
     * 评论id
     */
    private String commentRepayId;

    /**
     * 0：点赞
     * 1：取消点赞
     */
    private Short operation;
}