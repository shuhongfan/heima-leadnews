package com.heima.wemedia.service.impl;

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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mrchen
 * @date 2022/4/22 14:52
 */
@Service
public class WmUserServiceImpl extends ServiceImpl<WmUserMapper, WmUser> implements WmUserService {
    @Override
    public ResponseResult login(WmUserDTO dto) {
        // 1. 校验参数  name password
        String name = dto.getName();
        String password = dto.getPassword();
        if(StringUtils.isBlank(name)||StringUtils.isBlank(password)){
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"用户名或密码不能为空");
        }
        // 2. 根据用户名查询用户
        WmUser wmUser = this.getOne(Wrappers.<WmUser>lambdaQuery().eq(WmUser::getName, name));
        if(wmUser == null){
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"用户不存在");
        }
        // 3. 判断输入的密码 和 数据库中的密码是否一致
        String inputPwd = DigestUtils.md5DigestAsHex((dto.getPassword() + wmUser.getSalt()).getBytes());
        if(!inputPwd.equals(wmUser.getPassword())){
            CustException.cust(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        // 4. 判断user状态   9 正常
        Integer status = wmUser.getStatus();
        if(status.intValue()!=9){
            CustException.cust(AppHttpCodeEnum.LOGIN_STATUS_ERROR);
        }
        // 5. 修改最近登陆时间
        wmUser.setLoginTime(new Date());
        this.updateById(wmUser);
        // 6. 颁发凭证  AppJwtUtil.getToken (userId)
        String token = AppJwtUtil.getToken(Long.valueOf(wmUser.getId()));
        // 7. 封装返回结果   Map {  user:      token:  }
        WmUserVO userVO = new WmUserVO();
        BeanUtils.copyProperties(wmUser,userVO);
        Map result = new HashMap<>();
        result.put("token",token);
        result.put("user",userVO);
        return ResponseResult.okResult(result);
    }
}
