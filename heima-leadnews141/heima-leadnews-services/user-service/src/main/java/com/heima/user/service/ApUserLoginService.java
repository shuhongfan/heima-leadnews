package com.heima.user.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.LoginDTO;

public interface ApUserLoginService {
    /**
     * app端登录
     * @param dto
     * @return
     */
    public ResponseResult login(LoginDTO dto);
}