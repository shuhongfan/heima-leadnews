package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.exception.CustException;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.wemedia.mapper.WmUserMapper;
import com.heima.wemedia.service.WmUserService;
import org.springframework.stereotype.Service;

@Service
public class WmUserServiceImpl extends ServiceImpl<WmUserMapper, WmUser> implements WmUserService {

    /**
     * 根据名称查询自媒体用户信息
     * @param name
     * @return
     */
    @Override
    public ResponseResult<WmUser> findByName(String name) {
        WmUser wmUser = getOne(Wrappers.<WmUser>lambdaQuery().eq(WmUser::getName, name));
        if (wmUser != null) {
            return ResponseResult.okResult(wmUser);
        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"用户不存在");
        }
    }
}
