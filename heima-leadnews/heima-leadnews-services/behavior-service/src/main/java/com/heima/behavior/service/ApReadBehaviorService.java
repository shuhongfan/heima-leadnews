package com.heima.behavior.service;

import com.heima.model.behavior.dtos.ReadBehaviorDTO;
import com.heima.model.common.dtos.ResponseResult;

public interface ApReadBehaviorService {
    /**
     * 记录阅读行为
     * @param dto
     * @return
     */
    ResponseResult readBehavior(ReadBehaviorDTO dto);
}
