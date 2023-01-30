package com.heima.model.comment.dtos;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@Data
public class CommentSaveDTO {
    /**
     * 文章id
     */
    private Long articleId;
    /**
     * 评论内容
     */
    private String content;
}