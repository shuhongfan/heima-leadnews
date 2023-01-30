package com.heima.behavior.controller.v1;

import com.heima.behavior.service.ApCollectionBehaviorService;
import com.heima.model.behavior.dtos.CollectionBehaviorDTO;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "收藏行为API",tags = "收藏行为API")
@RestController
@RequestMapping("api/v1/collection_behavior/")
public class ApCollectionBehaviorController {
    @Autowired
    ApCollectionBehaviorService apCollectionBehaviorService;
    @ApiOperation("保存或取消 收藏行为")
    @PostMapping
    public ResponseResult collectArticle(@RequestBody CollectionBehaviorDTO dto){
        return apCollectionBehaviorService.collectBehavior(dto);
    }
}