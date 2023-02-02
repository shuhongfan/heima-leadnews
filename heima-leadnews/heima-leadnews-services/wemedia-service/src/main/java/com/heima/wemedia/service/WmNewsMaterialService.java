package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.wemedia.pojos.WmNewsMaterial;

public interface WmNewsMaterialService extends IService<WmNewsMaterial> {
    /**
     * 根据图片id查询引用数量
     * @param id
     * @return
     */
    Integer countNewsBymaterialId(Integer id);
}
