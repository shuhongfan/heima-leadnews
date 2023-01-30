package com.heima.feigns;

import com.heima.config.HeimaFeignAutoConfiguration;
import com.heima.feigns.fallback.AdminFeignFallback;
import com.heima.model.admin.pojo.AdChannel;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author mrchen
 * @date 2022/4/28 10:41
 */
@FeignClient(value = "leadnews-admin",fallbackFactory = AdminFeignFallback.class,configuration = HeimaFeignAutoConfiguration.class)
public interface AdminFeign {
    @PostMapping("/api/v1/sensitive/sensitives")
    public ResponseResult<List<String>> sensitives();

    @GetMapping("/api/v1/channel/one/{id}")
    public ResponseResult<AdChannel> findOne(@PathVariable Integer id);

    // ================新增接口方法  start ================
    @GetMapping("/api/v1/channel/channels")
    ResponseResult<List<AdChannel>> selectChannels();
    // ================新增接口方法  end ================
}
