package com.heima.feigns;

import com.heima.config.HeimaFeignAutoConfiguration;
import com.heima.feigns.fallback.AdminFeignFallback;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        value = "leadnews-admin",
        fallbackFactory = AdminFeignFallback.class,
        configuration = HeimaFeignAutoConfiguration.class
)
public interface AdminFeign {
    @ApiOperation(value = "查询敏感词内容list")
    @PostMapping("/api/v1/sensitive/sensitives")
    public ResponseResult sensitives();
}
