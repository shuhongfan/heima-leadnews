package com.heima.behavior.controller.v1;

import com.heima.behavior.service.ApCollectionBehaviorService;
import com.heima.model.behavior.dtos.CollectionBehaviorDTO;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("收藏行为API")
@RestController
@RequestMapping("/api/v1/collection_behavior/")
public class ApCollectionBehaviorController {

    @Autowired
    private ApCollectionBehaviorService apCollectionBehaviorService;

    @ApiOperation("保存或取消 收藏行为")
    @PostMapping
    public ResponseResult collectBehavior(@RequestBody @Validated CollectionBehaviorDTO dto) {
        return apCollectionBehaviorService.collectBehavior(dto);
    }

}
