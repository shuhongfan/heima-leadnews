package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.dtos.AdUserDTO;
import com.heima.model.admin.pojo.AdUser;
import com.heima.model.common.dtos.ResponseResult;

/**
 * @author mrchen
 * @date 2022/4/22 10:27
 */
public interface AdUserService extends IService<AdUser> {

    /**
     * 登录功能
     * @return  data: { token : 凭证 , user : 用户信息}
     */
    ResponseResult login(AdUserDTO dto);
}
