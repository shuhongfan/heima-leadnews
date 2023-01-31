package com.heima.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.admin.dtos.SensitiveDTO;
import com.heima.model.admin.pojo.AdSensitive;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.models.auth.In;

/**
 * 敏感词管理
 */
public interface AdSensitiveService extends IService<AdSensitive> {
    /**
     * 查询敏感词列表
     * @param sensitiveDTO
     * @return
     */
    public ResponseResult list(SensitiveDTO sensitiveDTO);


    /**
     * 新增敏感词
     * @param adSensitive
     * @return
     */
    public ResponseResult insert(AdSensitive adSensitive);

    /**
     * 修改敏感词
     * @param adSensitive
     * @return
     */
    public ResponseResult update(AdSensitive adSensitive);


    /**
     * 删除敏感词
     * @param id
     * @return
     */
    public ResponseResult delete(Integer id);
}
