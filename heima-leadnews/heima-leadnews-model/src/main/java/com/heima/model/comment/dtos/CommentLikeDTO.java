package com.heima.model.comment.dtos;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class CommentLikeDTO {

    /**
     * 评论id
     */
    @NotNull(message = "文章id不能为空")
    private String commentId;

    /**
     * 0：点赞
     * 1：取消点赞
     */
    @NotNull(message = "操作类型不能为空")
    @Range(min = 0,max = 1,message = "操作类型必须为0或1")
    private Short operation;
}