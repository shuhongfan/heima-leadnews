package com.heima.wemedia.controller.v1;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.wemedia.service.WmUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author mrchen
 * @date 2022/4/22 14:53
 */
@RestController
@RequestMapping("/api/v1/user")
@Api("自媒体用户controller")
public class WmUserController {
    @Autowired
    WmUserService wmUserService;
    @ApiOperation("根据用户名查询自媒体用户信息")
    @GetMapping("/findByName/{name}")
    public ResponseResult findByName(@PathVariable("name") String name){
        WmUser wmUser = wmUserService.getOne(Wrappers.<WmUser>lambdaQuery().eq(WmUser::getName, name));
        return ResponseResult.okResult(wmUser);
    }
    @ApiOperation("保存自媒体用户信息")
    @PostMapping("/save")
    public ResponseResult save(@RequestBody WmUser wmUser){
        // wmUser 新增户名 是没有ID
        wmUserService.save(wmUser);
        // wmUser 是有id  是由mybatisplus 回填的id
        return ResponseResult.okResult(wmUser);
    }
}
