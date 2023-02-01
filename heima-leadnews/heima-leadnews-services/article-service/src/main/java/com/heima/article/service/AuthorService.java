package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;

public interface AuthorService extends IService<ApAuthor> {
    /**
     * 根据appUserId查询关联作者信息
     * @param userId
     * @return
     */
    ResponseResult<ApAuthor> findByUserId(Integer userId);
}
