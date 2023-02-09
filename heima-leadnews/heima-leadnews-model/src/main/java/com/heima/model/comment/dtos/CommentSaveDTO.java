package com.heima.model.comment.dtos;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CommentSaveDTO {
    /**
     * 文章id
     */
    @NotNull(message = "文章id不能为空")
    private Long articleId;


    /**
     * 评论内容
     */
    @NotNull(message = "评论内容不能为空")
    @Size(min = 1,max = 140,message = "评论内容不能大于140个字符")
    private String content;
}