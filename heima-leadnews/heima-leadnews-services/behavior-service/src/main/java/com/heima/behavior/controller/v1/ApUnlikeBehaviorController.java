package com.heima.behavior.controller.v1;

import com.heima.behavior.service.ApUnlikeBehaviorService;
import com.heima.model.behavior.dtos.UnLikesBehaviorDTO;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("不喜欢行为API")
@RestController
@RequestMapping("/api/v1/un_likes_behavior/")
public class ApUnlikeBehaviorController {

    @Autowired
    private ApUnlikeBehaviorService apUnlikeBehaviorService;

    @ApiOperation("保存或取消 不喜欢行为")
    @PostMapping
    public ResponseResult unlikeBehavior(@RequestBody @Validated UnLikesBehaviorDTO dto) {
        return apUnlikeBehaviorService.unlikeBehavior(dto);
    }

}
