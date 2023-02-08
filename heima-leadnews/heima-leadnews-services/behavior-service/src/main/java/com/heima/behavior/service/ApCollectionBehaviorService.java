package com.heima.behavior.service;

import com.heima.model.behavior.dtos.CollectionBehaviorDTO;
import com.heima.model.common.dtos.ResponseResult;

public interface ApCollectionBehaviorService {
    /**
     * 收藏 取消收藏
     * @param dto
     * @return
     */
    ResponseResult collectBehavior(CollectionBehaviorDTO dto);
}
