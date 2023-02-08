package com.heima.behavior.controller.v1;

import com.heima.behavior.service.ApLikesBehaviorService;
import com.heima.model.behavior.dtos.LikesBehaviorDTO;
import com.heima.model.behavior.pojos.ApLikesBehavior;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api("喜欢行为API")
@RestController
@RequestMapping("/api/v1/likes_behavior")
public class ApLikesBehaviorController {
    @Autowired
    private ApLikesBehaviorService apLikesBehaviorService;

    /**
     * 点赞或取消点赞
     * @param dto
     * @return
     */
    @PostMapping
    public ResponseResult like(@RequestBody @Validated LikesBehaviorDTO dto) {
        return apLikesBehaviorService.like(dto);
    }
}
