package com.heima.feigns;

import com.heima.config.HeimaFeignAutoConfiguration;
import com.heima.feigns.fallback.BehaviorFeignFallback;
import com.heima.model.behavior.pojos.ApBehaviorEntry;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "leadnews-behavior",
        fallbackFactory = BehaviorFeignFallback.class,
        configuration = HeimaFeignAutoConfiguration.class)
public interface BehaviorFeign {
   @GetMapping("/api/v1/behavior_entry/one")
    public ResponseResult<ApBehaviorEntry> findByUserIdOrEquipmentId(@RequestParam("userId") Integer userId,
                                                                     @RequestParam("equipmentId") Integer equipmentId);
}