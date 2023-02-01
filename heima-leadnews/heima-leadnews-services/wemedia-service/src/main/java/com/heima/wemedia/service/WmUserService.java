package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmUser;

public interface WmUserService extends IService<WmUser> {
    /**
     * 根据名称查询自媒体用户信息
     * @param name
     * @return
     */
    public ResponseResult<WmUser> findByName(String name);
}
