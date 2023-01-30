package com.heima.feigns;
import com.heima.config.HeimaFeignAutoConfiguration;
import com.heima.feigns.fallback.UserFeignFallback;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.pojos.ApUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(value = "leadnews-user", // 调用服务
        fallbackFactory = UserFeignFallback.class, // 服务降级
        configuration = HeimaFeignAutoConfiguration.class) // feign日志配置
public interface UserFeign {
    @GetMapping("/api/v1/user/{id}")
    ResponseResult<ApUser> findUserById(@PathVariable("id") Integer id);
}