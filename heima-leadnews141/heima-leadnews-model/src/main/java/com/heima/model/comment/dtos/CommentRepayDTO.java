package com.heima.model.comment.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class CommentRepayDTO {
    /**
     * 评论id
     */
    private String commentId;
    private Integer size;
    // 最小时间
    private Date minDate;
}