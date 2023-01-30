package com.heima.model.comment.dtos;

import lombok.Data;

@Data
public class CommentRepayLikeDTO {
    /**
     * 回复id
     */
    private String commentRepayId;
    /**
     * 0：点赞
     * 1：取消点赞
     */
    private Short operation;
}