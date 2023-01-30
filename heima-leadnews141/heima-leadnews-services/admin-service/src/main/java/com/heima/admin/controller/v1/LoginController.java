package com.heima.admin.controller.v1;

import com.heima.admin.service.AdUserService;
import com.heima.model.admin.dtos.AdUserDTO;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "运营平台登录API",tags = "运营平台登录API")
@RestController
@RequestMapping("/login")
public class LoginController{
    @Autowired
    AdUserService userService;
    @ApiOperation("登录")
    @PostMapping("/in")
    public ResponseResult login(@RequestBody AdUserDTO DTO) {
        return userService.login(DTO);
    }
}