package com.heima.model.comment.dtos;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotNull;
@Data
public class CommentLikeDTO {

    /**
     * 评论id
     */
    @NotNull(message = "评论id不能为空")
    private String commentId;

    /**
     * 0：点赞
     * 1：取消点赞
     */
    @Range(min = 0,max = 1,message = "operation的值必须是0 或 1")
    private Short operation;
}