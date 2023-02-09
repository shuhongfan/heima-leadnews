package com.heima.model.comment.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CommentRepaySaveDTO {
    /**
     * 评论id
     */
    @NotNull(message = "评论id不能为空")
    private String commentId;
    /**
     * 回复内容
     */
    @NotNull(message = "回复内容不能为空")
    @Size(min = 1,max = 140)
    private String content;
}