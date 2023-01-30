package com.heima.feigns.fallback;

import com.heima.feigns.BehaviorFeign;
import com.heima.model.behavior.pojos.ApBehaviorEntry;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BehaviorFeignFallback implements FallbackFactory<BehaviorFeign> {
    @Override
    public BehaviorFeign create(Throwable throwable) {
        throwable.printStackTrace();
        return new BehaviorFeign() {
            @Override
            public ResponseResult<ApBehaviorEntry> findByUserIdOrEquipmentId(Integer userId, Integer equipmentId) {
                log.error("Feign服务降级触发 远程调用:BehaviorFeign  findByUserIdOrEquipmentId 失败,参数:userId={} equipmentId={}",userId,equipmentId);
                return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR,"远程服务调用出现异常");
            }
        };
    }
}