package com.heima.model.comment.vos;

import com.heima.model.comment.pojos.ApCommentRepay;
import lombok.Data;

@Data
public class ApCommentRepayVO extends ApCommentRepay {
    /**
     * 0：点赞
     * 1：取消点赞
     */
    private Short operation;
}