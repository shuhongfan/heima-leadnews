package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.mapper.AdUserMapper;
import com.heima.admin.service.AdUserService;
import com.heima.common.exception.CustException;
import com.heima.model.admin.dtos.AdUserDTO;
import com.heima.model.admin.pojo.AdUser;
import com.heima.model.admin.vos.AdUserVO;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.utils.common.AppJwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdUserServiceImpl extends ServiceImpl<AdUserMapper,AdUser> implements AdUserService {
    @Transactional
    @Override
    public ResponseResult login(AdUserDTO dto) {
        // 1. 校验参数   保证 name  password 不为空
        String name = dto.getName();
        String password = dto.getPassword();
        if(StringUtils.isBlank(name)||StringUtils.isBlank(password)){
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"用户名或密码不能为空");
        }
        // 2. 根据name 查询用户信息
        AdUser adUser = this.getOne(Wrappers.<AdUser>lambdaQuery().eq(AdUser::getName, name));
        if (adUser == null){
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"用户不存在");
        }
        // 3. 判断输入的密码是否和数据库中密码一致
        String inputPwd = DigestUtils.md5DigestAsHex((password + adUser.getSalt()).getBytes());
        if(!inputPwd.equals(adUser.getPassword())){
            CustException.cust(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR,"用户密码错误");
        }
        // 4. 判断用户状态是否正确  9(正常)
        if (adUser.getStatus().intValue() != 9) {
            CustException.cust(AppHttpCodeEnum.LOGIN_STATUS_ERROR);
        }
        // 5. 修改最近登陆时间
        adUser.setLoginTime(new Date());
        this.updateById(adUser);
        // 6. 颁发token
        String token = AppJwtUtil.getToken(Long.valueOf(adUser.getId()));

        // 7. 封装返回结果 token  user
        Map result = new HashMap<>();
        result.put("token",token);
        // 通过BeanUtils拷贝 同名属性
        AdUserVO adUserVO = new AdUserVO();
        BeanUtils.copyProperties(adUser,adUserVO);
        result.put("user",adUserVO);
        return ResponseResult.okResult(result);
    }
}
