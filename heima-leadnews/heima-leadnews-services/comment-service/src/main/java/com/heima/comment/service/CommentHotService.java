package com.heima.comment.service;

import com.heima.model.comment.pojos.ApComment;
import com.heima.model.comment.dtos.CommentDTO;
import com.heima.model.common.dtos.ResponseResult;

public interface CommentHotService {

    /**
     * 查找热点评论
     */
    public void hotCommentExecutor(ApComment apComment);

    /**
     * 根据 文章id 查询评论列表
     * @param dto
     * @return
     */
    public ResponseResult findByArticleId(CommentDTO dto);
}
