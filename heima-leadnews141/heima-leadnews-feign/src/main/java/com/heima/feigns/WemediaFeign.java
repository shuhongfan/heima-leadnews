package com.heima.feigns;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.config.HeimaFeignAutoConfiguration;
import com.heima.feigns.fallback.WemediaFeignFallback;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmUser;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author mrchen
 * @date 2022/4/22 15:01
 */
@FeignClient(value = "leadnews-wemedia",  // 微服务的名称
        fallbackFactory = WemediaFeignFallback.class, // 服务降级实现类
        configuration = HeimaFeignAutoConfiguration.class // feign的配置 日志级别
)
public interface WemediaFeign {
    @GetMapping("/api/v1/user/findByName/{name}")
    public ResponseResult<WmUser> findByName(@PathVariable("name") String name);
    @PostMapping("/api/v1/user/save")
    public ResponseResult<WmUser> save(@RequestBody WmUser wmUser);

    //===================== 新增接口  start ====================
    @GetMapping("/api/v1/news/one/{id}")
    public ResponseResult<WmNews> findWmNewsById(@PathVariable("id") Integer id);
    @PutMapping("/api/v1/news/update")
    ResponseResult updateWmNews(@RequestBody WmNews wmNews);
    //===================== 新增接口 end ====================
}
