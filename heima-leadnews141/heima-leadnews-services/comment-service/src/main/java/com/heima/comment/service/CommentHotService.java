package com.heima.comment.service;
import com.heima.model.comment.pojos.ApComment;
public interface CommentHotService {
    /**
     * 查找热点评论
     */
    public void hotCommentExecutor(ApComment apComment);
}