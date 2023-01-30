package com.heima.behavior.service;

import com.heima.model.behavior.dtos.ArticleBehaviorDTO;
import com.heima.model.common.dtos.ResponseResult;

public interface ApArticleBehaviorService {

    /**
     * 加载文章详情 数据回显
     * @param dto
     * @return
     */
    public ResponseResult loadArticleBehavior(ArticleBehaviorDTO dto);
}