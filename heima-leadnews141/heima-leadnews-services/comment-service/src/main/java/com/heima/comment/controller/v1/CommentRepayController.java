package com.heima.comment.controller.v1;
import com.heima.comment.service.CommentRepayService;
import com.heima.model.comment.dtos.CommentRepayDTO;
import com.heima.model.comment.dtos.CommentRepayLikeDTO;
import com.heima.model.comment.dtos.CommentRepaySaveDTO;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Api(tags = "评论回复API",value = "评论回复API")
@RestController
@RequestMapping("/api/v1/comment_repay")
public class CommentRepayController {
    @Autowired
    private CommentRepayService commentRepayService;
    @ApiOperation("加载评论回复列表")
    @PostMapping("/load")
    public ResponseResult loadCommentRepay(@RequestBody CommentRepayDTO dto){
       return commentRepayService.loadCommentRepay(dto);
    }
    @ApiOperation("保存评论回复")
    @PostMapping("/save")
    public ResponseResult saveCommentRepay(@RequestBody CommentRepaySaveDTO dto){
        return commentRepayService.saveCommentRepay(dto);
    }
    @ApiOperation("保存评论回复点赞")
    @PostMapping("/like")
    public ResponseResult saveCommentRepayLike(
      @RequestBody CommentRepayLikeDTO dto){
       return commentRepayService.saveCommentRepayLike(dto);
    }
}