package com.heima.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.AuthorMapper;
import com.heima.article.service.AuthorService;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl extends ServiceImpl<AuthorMapper, ApAuthor> implements AuthorService {
    /**
     * 根据appUserId查询关联作者信息
     * @param userId
     * @return
     */
    @Override
    public ResponseResult<ApAuthor> findByUserId(Integer userId) {
        LambdaQueryWrapper<ApAuthor> apAuthorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        apAuthorLambdaQueryWrapper.eq(ApAuthor::getUserId, userId);
        ApAuthor one = getOne(apAuthorLambdaQueryWrapper);
        return ResponseResult.okResult(one);
    }

}
