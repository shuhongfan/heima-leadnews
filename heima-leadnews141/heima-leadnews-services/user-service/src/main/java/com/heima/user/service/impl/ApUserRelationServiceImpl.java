package com.heima.user.service.impl;

import com.heima.common.exception.CustException;
import com.heima.model.common.constants.user.UserRelationConstants;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.threadlocal.AppThreadLocalUtils;
import com.heima.model.user.dtos.UserRelationDTO;
import com.heima.model.user.pojos.ApUser;
import com.heima.user.service.ApUserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

/**
 * @author mrchen
 * @date 2022/5/2 15:17
 */
@Service
public class ApUserRelationServiceImpl implements ApUserRelationService {

    @Autowired
    StringRedisTemplate redisTemplate;


    @Override
    public ResponseResult follow(UserRelationDTO dto) {
        // 1. 检查参数  (是否登陆   operation: 0 1   自己不能关注自己   是否已经关注)
        ApUser loginUser = AppThreadLocalUtils.getUser();
        if (loginUser == null) {
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN);
        }
        int operation = dto.getOperation().intValue();
        if(operation!=0 && operation!=1){
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"关注类型错误  类型必须为: 0关注  1取关");
        }
        // 用于操作zset接口的 redis对象
        ZSetOperations<String, String> zsetOption = redisTemplate.opsForZSet();

        // 开启支持事务
        redisTemplate.setEnableTransactionSupport(true);// 开启支持事务
        if(operation == 0){
            // 2. operation = 0  关注     关注集合   粉丝集合 添加数据
            if(loginUser.getId().equals(dto.getAuthorApUserId())){
                CustException.cust(AppHttpCodeEnum.DATA_NOT_ALLOW,"自己不能关注自己");
            }

            redisTemplate.multi();
            try {
                // zscore  集合   元素      有返回值: 有这个元素    返回值null: 没有这个元素
                Double score = zsetOption.score(UserRelationConstants.FOLLOW_LIST + loginUser.getId(), String.valueOf(dto.getAuthorApUserId()));
                if (score!=null) {
                    CustException.cust(AppHttpCodeEnum.DATA_NOT_ALLOW,"请勿重复关注");
                }

            // 将作者id 添加到我的关注集合中

                zsetOption.add(UserRelationConstants.FOLLOW_LIST + loginUser.getId(), String.valueOf(dto.getAuthorApUserId()), System.currentTimeMillis());
                zsetOption.add(UserRelationConstants.FANS_LIST + dto.getAuthorApUserId(), String.valueOf(loginUser.getId()), System.currentTimeMillis());
                redisTemplate.exec();
            } catch (Exception e) {
                e.printStackTrace();
                redisTemplate.discard();
            }
        }else {
            // 3. operation = 1  取关     关注集合   粉丝集合 删除数据
            zsetOption.remove(UserRelationConstants.FOLLOW_LIST + loginUser.getId(),String.valueOf(dto.getAuthorApUserId()));
            zsetOption.remove(UserRelationConstants.FANS_LIST + dto.getAuthorApUserId(),String.valueOf(loginUser.getId()));
        }
        return ResponseResult.okResult();
    }
}
