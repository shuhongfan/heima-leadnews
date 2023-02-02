package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.exception.CustException;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.pojos.WmNewsMaterial;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmNewsMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WmNewsMaterialServiceImpl extends ServiceImpl<WmNewsMaterialMapper, WmNewsMaterial> implements WmNewsMaterialService {

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    /**
     * 根据图片id查询引用数量
     * @param id
     * @return
     */
    @Override
    public Integer countNewsBymaterialId(Integer id) {
        if (id == null) {
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"图片id不能为空");
        }

        LambdaQueryWrapper<WmNewsMaterial> query = Wrappers.<WmNewsMaterial>lambdaQuery();
        query.eq(WmNewsMaterial::getMaterialId, id);
        return count(query);
    }
}
