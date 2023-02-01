package com.heima.user.controller;

import com.heima.common.constants.admin.AdminConstants;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.AuthDTO;
import com.heima.user.service.ApUserRealnameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "app用户实名认证API", tags = "app用户实名认证API")
@RestController
@RequestMapping("/api/v1/auth")
public class ApUserRealnameController {
    @Autowired
    private ApUserRealnameService apUserRealnameService;

    @ApiOperation("根据状态查询实名认证列表")
    @PostMapping("/list")
    public ResponseResult loadListByStatus(@RequestBody AuthDTO authDTO){
        return apUserRealnameService.loadListByStatus(authDTO);
    }

    @ApiOperation("实名认证通过")
    @PostMapping("/authPass")
    public ResponseResult authPass(@RequestBody AuthDTO DTO) {
        return apUserRealnameService.updateStatusById(DTO, AdminConstants.PASS_AUTH);
    }

    @ApiOperation("实名认证失败")
    @PostMapping("/authFail")
    public ResponseResult authFail(@RequestBody AuthDTO DTO) {
        return apUserRealnameService.updateStatusById(DTO, AdminConstants.FAIL_AUTH);
    }
}
