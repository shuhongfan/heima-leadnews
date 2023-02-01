package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.dtos.AdUserDTO;
import com.heima.model.admin.pojo.AdUser;
import com.heima.model.common.dtos.ResponseResult;

/**
 * 管理员用户信息
 */
public interface AdUserService extends IService<AdUser> {
    /**
     * 登录功能
     * @param adUserDTO
     * @return
     */
    ResponseResult login(AdUserDTO adUserDTO);

}
