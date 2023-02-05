package com.heima.feigns;

import com.heima.config.HeimaFeignAutoConfiguration;
import com.heima.feigns.fallback.WemediaFeignFallback;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmUser;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        value = "leadnews-wemedia", // 微服务的名称
        fallbackFactory = WemediaFeignFallback.class, // 服务降级实现类
        configuration = HeimaFeignAutoConfiguration.class) // feign的配置  日志级别
public interface WemediaFeign {
    @ApiOperation("保存自媒体用户信息")
    @PostMapping("/api/v1/user/save")
    public ResponseResult<WmUser> save(@RequestBody @Validated WmUser wmUser);

    @ApiOperation("根据名称查询自媒体用户信息")
    @GetMapping("/api/v1/user/findByName/{name}")
    public ResponseResult<WmUser> findByName(@PathVariable("name") String name);

    @ApiOperation(value = "根据id查询自媒体文章")
    @GetMapping("/api/v1/news/one/{id}")
    public ResponseResult<WmNews> findWmNewsById(@PathVariable("id") Integer id);

    @ApiOperation("根据id修改自媒体文章")
    @PutMapping("/api/v1/news/update")
    ResponseResult updateWmNews(@RequestBody WmNews wmNews);
}
