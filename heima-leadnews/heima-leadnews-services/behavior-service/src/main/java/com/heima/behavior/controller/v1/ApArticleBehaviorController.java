package com.heima.behavior.controller.v1;

import com.heima.behavior.service.ApArticleBehaviorService;
import com.heima.model.behavior.dtos.ArticleBehaviorDTO;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api("文章行为API")
@RestController
@RequestMapping("/api/v1/article/load_article_behavior")
public class ApArticleBehaviorController {

    @Autowired
    private ApArticleBehaviorService apArticleBehaviorService;

    @ApiOperation("查询文章行为相关信息")
    @PostMapping
    public ResponseResult loadArticleBehavior(@RequestParam @Validated ArticleBehaviorDTO dto) {
        return apArticleBehaviorService.loadArticleBehavior(dto);
    }

}
