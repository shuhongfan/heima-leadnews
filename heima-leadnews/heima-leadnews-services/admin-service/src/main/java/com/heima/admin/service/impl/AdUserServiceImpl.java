package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.mapper.AdUserMapper;
import com.heima.admin.service.AdUserService;
import com.heima.common.exception.CustException;
import com.heima.model.admin.dtos.AdUserDTO;
import com.heima.model.admin.pojo.AdUser;
import com.heima.model.admin.vo.AdUserVO;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.utils.common.AppJwtUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;

/**
 * 管理员用户信息
 */
@Service
public class AdUserServiceImpl extends ServiceImpl<AdUserMapper,AdUser> implements AdUserService {

    /**
     * 登录功能
     * @param adUserDTO
     * @return
     */
    @Override
    public ResponseResult login(AdUserDTO adUserDTO) {
//        1.校验参数 保证name password  不为空
        String name = adUserDTO.getName();
        String password = adUserDTO.getPassword();
        if (StringUtils.isBlank(name) || StringUtils.isBlank(password)) {
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"用户名密码不能为空");
        }

//        2. 根据name 查询用户信息
        LambdaQueryWrapper<AdUser> adUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        adUserLambdaQueryWrapper.eq(AdUser::getName, adUserDTO.getName());
        AdUser adUser = getOne(adUserLambdaQueryWrapper);
        if (adUser == null) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST, "用户名不存在");
        }

//        3. 判断输入的密码是否和数据库中密码一致
        String inputPwd = DigestUtils.md5DigestAsHex((password + adUser.getSalt()).getBytes());
        if (!inputPwd.equals(adUser.getPassword())) {
            CustException.cust(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR, "用户密码错误");
        }

//        4. 判断用户状态是否正确 9正常
        if (adUser.getStatus().intValue()!=9) {
            CustException.cust(AppHttpCodeEnum.LOGIN_STATUS_ERROR);
        }

//        5. 修改最近登录时间
        adUser.setLoginTime(new Date());
        updateById(adUser);

//        6.颁发token
        String token = AppJwtUtil.getToken(Long.valueOf(adUser.getId()));

//        7. 封装返回结果 token user
        HashMap<String, Object> result = new HashMap<>();
        result.put("token", token);

        AdUserVO adUserVO = new AdUserVO();
//        通过BeanUtils拷贝 同名属性
        BeanUtils.copyProperties(adUser,adUserVO);
        result.put("user", adUserVO);

        return ResponseResult.okResult(result);
    }
}
