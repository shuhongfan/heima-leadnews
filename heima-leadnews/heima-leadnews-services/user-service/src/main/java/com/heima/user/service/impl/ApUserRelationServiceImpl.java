package com.heima.user.service.impl;

import com.heima.common.constants.user.UserRelationConstants;
import com.heima.common.exception.CustException;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.threadlocal.AppThreadLocalUtils;
import com.heima.model.user.dtos.UserRelationDTO;
import com.heima.model.user.pojos.ApUser;
import com.heima.user.service.ApUserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
public class ApUserRelationServiceImpl implements ApUserRelationService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 用户关注/取消关注
     * @param dto
     * @return
     */
    @Override
    public ResponseResult follow(UserRelationDTO dto) {
//        1. 校验参数    authorApUserId   必须登录   operation 0  1
        ApUser user = AppThreadLocalUtils.getUser();
        if (dto.getAuthorApUserId() == null) {
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN);
        }

        int operation = dto.getOperation().intValue();
        if (operation != 0 && operation != 1) {
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID, "关注类型错误 类型必须为: 0关注 1取关");
        }

//        用于操作Zset接口的redis对象
        ZSetOperations<String,String> zSetOperations = redisTemplate.opsForZSet();

        if (operation == 0) {
//        2. opertion=0 关注 关注集合 粉丝集合  添加数据
            if (user.getId().equals(dto.getAuthorApUserId())) {
                CustException.cust(AppHttpCodeEnum.DATA_NOT_ALLOW, "自己不能关注自己");
            }

//            zscore 来查找集合元素  有返回值:有这个元素  返回null:没有这个给元素
            Double score = zSetOperations.score(UserRelationConstants.FOLLOW_LIST + user.getId(), dto.getAuthorApUserId().toString());
            if (score != null) {
                CustException.cust(AppHttpCodeEnum.DATA_NOT_ALLOW,"请勿重复关注");
            }

//            将作者id 添加到我的关注集合中
            zSetOperations.add(UserRelationConstants.FOLLOW_LIST + user.getId(), dto.getAuthorApUserId().toString(), System.currentTimeMillis());
            zSetOperations.add(UserRelationConstants.FANS_LIST + dto.getAuthorApUserId(), user.getId().toString(), System.currentTimeMillis());
        } else {
//        3. operation=1 取关  关注集合 粉丝集合 删除数据
            zSetOperations.remove(UserRelationConstants.FOLLOW_LIST + user.getId(), dto.getAuthorApUserId().toString(), System.currentTimeMillis());
            zSetOperations.remove(UserRelationConstants.FANS_LIST + dto.getAuthorApUserId(), user.getId().toString(), System.currentTimeMillis());
        }

        return ResponseResult.okResult();
    }

}
