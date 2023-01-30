package com.heima.behavior.controller.v1;
import com.heima.behavior.service.ApArticleBehaviorService;
import com.heima.model.behavior.dtos.ArticleBehaviorDTO;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Api(value = "文章行为api",tags = "文章行为api")
@RestController
@RequestMapping("/api/v1/article")
public class ArticleBehaviorController {
    @Autowired
    ApArticleBehaviorService apArticleBehaviorService;
    @ApiOperation("查询文章行为相关信息")
    @PostMapping("/load_article_behavior")
    public ResponseResult loadArticleBehavior(@RequestBody ArticleBehaviorDTO dto){
        return apArticleBehaviorService.loadArticleBehavior(dto);
    }
}