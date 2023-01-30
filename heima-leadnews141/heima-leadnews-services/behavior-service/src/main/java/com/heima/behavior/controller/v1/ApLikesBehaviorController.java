package com.heima.behavior.controller.v1;

import com.heima.behavior.service.ApLikesBehaviorService;
import com.heima.model.behavior.dtos.LikesBehaviorDTO;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("点赞行为API")
@RestController
@RequestMapping("/api/v1/likes_behavior")
public class ApLikesBehaviorController{
    @Autowired
    private ApLikesBehaviorService apLikesBehaviorService;
    @ApiOperation("点赞或取消点赞")
    @PostMapping
    public ResponseResult like(@RequestBody @Validated LikesBehaviorDTO dto) {
        return apLikesBehaviorService.like(dto);
    }
}