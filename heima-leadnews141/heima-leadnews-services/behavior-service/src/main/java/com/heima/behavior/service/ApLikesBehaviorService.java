package com.heima.behavior.service;
import com.heima.model.behavior.dtos.LikesBehaviorDTO;
import com.heima.model.common.dtos.ResponseResult;
public interface ApLikesBehaviorService {
    /**
     * 点赞或取消点赞
     * @param dto
     * @return
     */
	public ResponseResult like(LikesBehaviorDTO dto);
}