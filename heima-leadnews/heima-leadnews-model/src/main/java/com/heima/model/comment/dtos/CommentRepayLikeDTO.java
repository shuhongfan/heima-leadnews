package com.heima.model.comment.dtos;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class CommentRepayLikeDTO {
    /**
     * 回复id
     */
    @NotNull(message = "回复id不能为空")
    private String commentRepayId;
    /**
     * 0：点赞
     * 1：取消点赞
     */
    @NotNull(message = "操作不能为空")
    @Range(min = 0, max = 1, message = "操作数只能是0或1")
    private Short operation;
}