package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.exception.CustException;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.dtos.WmUserDTO;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.model.wemedia.vos.WmUserVO;
import com.heima.utils.common.AppJwtUtil;
import com.heima.wemedia.mapper.WmUserMapper;
import com.heima.wemedia.service.WmUserService;
import io.seata.common.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

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

    /**
     * 登录
     * @param dto
     * @return
     */
    @Override
    public ResponseResult login(WmUserDTO dto) {
//        1. 校验参数 name password
        String name = dto.getName();
        String password = dto.getPassword();

        if (StringUtils.isBlank(name) || StringUtils.isBlank(password)) {
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"用户名密码不能为空");
        }

//        2. 根据用户名查询用户
        LambdaQueryWrapper<WmUser> wrapper = Wrappers.<WmUser>lambdaQuery().eq(WmUser::getName, name);
        WmUser wmUser = getOne(wrapper);
        if (wmUser == null) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"用户不存在");
        }

//        3. 判断输入的密码和数据库中的密码是否一致
        String inputPWD = DigestUtils.md5DigestAsHex((dto.getPassword() + wmUser.getSalt()).getBytes());
        if (!inputPWD.equals(wmUser.getPassword())) {
            CustException.cust(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR,"用户名密码错误");
        }

//        4. 判断 user 状态 9正常
        Integer status = wmUser.getStatus();
        if (status.intValue() != 9) {
            CustException.cust(AppHttpCodeEnum.LOGIN_STATUS_ERROR);
        }

//        5. 修改最近登录时间
        wmUser.setLoginTime(new Date());
        updateById(wmUser);

//        6. 颁发凭证
        String token = AppJwtUtil.getToken(Long.valueOf(wmUser.getId()));

//        7.封装返回结果
        WmUserVO wmUserVO = new WmUserVO();
        BeanUtils.copyProperties(wmUser, wmUserVO);

        HashMap<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user", wmUserVO);
        return ResponseResult.okResult(map);
    }
}
