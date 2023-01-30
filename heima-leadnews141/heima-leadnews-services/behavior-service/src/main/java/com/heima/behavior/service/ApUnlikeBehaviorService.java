package com.heima.behavior.service;
import com.heima.model.behavior.dtos.UnLikesBehaviorDTO;
import com.heima.model.common.dtos.ResponseResult;
public interface ApUnlikeBehaviorService {
    /**
     * 保存 或 取消 不喜欢
     * @param dto
     * @return
     */
    ResponseResult unlikeBehavior(UnLikesBehaviorDTO dto);
}
