package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.common.exception.CustException;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.LoginDTO;
import com.heima.model.user.pojos.ApUser;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.service.ApUserLoginService;
import com.heima.utils.common.AppJwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author mrchen
 * @date 2022/5/2 9:28
 */
@Service
public class ApUserLoginServiceImpl implements ApUserLoginService {

    @Autowired
    ApUserMapper apUserMapper;

    @Override
    public ResponseResult login(LoginDTO dto) {
        // 1. 判断手机号 密码 是否为空
        String phone = dto.getPhone();
        String password = dto.getPassword();
        if(StringUtils.isNotBlank(phone)&&StringUtils.isNotBlank(password)){
            //    1.1 如果不为空 查询手机号对应的用户是否存在
            ApUser apUser = apUserMapper.selectOne(Wrappers.<ApUser>lambdaQuery().eq(ApUser::getPhone, phone));
            if(Objects.isNull(apUser)){
                CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"对应的用户信息不存在");
            }
            //    1.2 对比输入的密码和 数据库中密码是否一致
            String dbPassword = apUser.getPassword();
            String inputPassword = DigestUtils.md5DigestAsHex((password + apUser.getSalt()).getBytes());
            if(!inputPassword.equals(dbPassword)){
                CustException.cust(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR,"密码错误");
            }
            //    1.3 颁发token 并返回  （user    token  ）
            String token = AppJwtUtil.getToken(Long.valueOf(apUser.getId()));
            Map result = new HashMap<>();
            result.put("token",token);
            apUser.setSalt("");
            apUser.setPassword("");
            result.put("user",apUser);
            return ResponseResult.okResult(result);
        }else {
            // 2. 如果手机号或密码为空 采用设备id登陆
            if(dto.getEquipmentId() == null){
                //    2.1  判断设备id是否存在
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
            }
            //    2.2  直接颁发token  userId存储 0
            String token = AppJwtUtil.getToken(0L);
            Map result = new HashMap<>();
            result.put("token",token);
            return ResponseResult.okResult(result);
        }
    }
}
