package com.heima.user.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.LoginDTO;
import com.heima.user.service.ApUserLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "app端用户登录api",tags = "app端用户登录api")
@RestController
@RequestMapping("/api/v1/login")
public class ApUserLoginController {

    @Autowired
    private ApUserLoginService apUserLoginService;

    @ApiOperation("登录")
    @PostMapping("/login_auth")
    public ResponseResult login(@RequestBody LoginDTO dto) {
        return apUserLoginService.login(dto);
    }

}