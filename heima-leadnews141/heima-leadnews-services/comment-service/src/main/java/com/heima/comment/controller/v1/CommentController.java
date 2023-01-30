package com.heima.comment.controller.v1;

import com.heima.comment.service.CommentService;
import com.heima.model.comment.dtos.CommentDTO;
import com.heima.model.comment.dtos.CommentLikeDTO;
import com.heima.model.comment.dtos.CommentSaveDTO;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "评论管理API",tags = "评论管理API")
@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {
  @Autowired
  private CommentService commentService;
  @ApiOperation("保存评论信息")
  @PostMapping("/save")
  public ResponseResult saveComment(@RequestBody @Validated CommentSaveDTO dto){
    return commentService.saveComment(dto);
  }

  @ApiOperation("保存评论点赞")
  @PostMapping("/like")
  public ResponseResult like(@RequestBody CommentLikeDTO dto){
    return commentService.like(dto);
  }

  @ApiOperation("加载文章评论列表")
  @PostMapping("/load")
  public ResponseResult findByArticleId(@RequestBody CommentDTO dto){
    return commentService.findByArticleId(dto);
  }
}