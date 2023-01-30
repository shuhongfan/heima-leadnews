package com.heima.user.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.AuthDTO;
import com.heima.model.user.pojos.ApUserRealname;
public interface ApUserRealnameService extends IService<ApUserRealname> {
    /**
     * 根据状态查询需要认证相关的用户信息
     * @param DTO
     * @return
     */
    ResponseResult loadListByStatus(AuthDTO DTO);


    /**
     * 根据状态进行审核
     * @param dto
     * @param status  2 审核失败   9 审核成功
     * @return
     */
    ResponseResult updateStatusById(AuthDTO dto, Short status);
}