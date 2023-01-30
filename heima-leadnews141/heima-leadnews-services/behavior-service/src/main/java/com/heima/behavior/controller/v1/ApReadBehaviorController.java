package com.heima.behavior.controller.v1;

import com.heima.behavior.service.ApReadBehaviorService;
import com.heima.model.behavior.dtos.ReadBehaviorDTO;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("阅读行为api")
@RestController
@RequestMapping("/api/v1/read_behavior")
public class ApReadBehaviorController  {
    @Autowired
    ApReadBehaviorService apReadBehaviorService;
    @ApiOperation("保存阅读行为")
    @PostMapping
    public ResponseResult readBehavior(@RequestBody ReadBehaviorDTO dto) {
        return apReadBehaviorService.readBehavior(dto);
    }
}