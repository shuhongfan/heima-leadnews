package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.mapper.AdSensitiveMapper;
import com.heima.admin.service.AdSensitiveService;
import com.heima.model.admin.dtos.SensitiveDTO;
import com.heima.model.admin.pojo.AdSensitive;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 敏感词管理
 */
@Service
@Transactional
public class AdSensitiveServiceImpl extends ServiceImpl<AdSensitiveMapper, AdSensitive> implements AdSensitiveService {
    @Autowired
    private AdSensitiveMapper adSensitiveMapper;

    /**
     * 查询敏感词列表
     * @param sensitiveDTO
     * @return
     */
    @Override
    public ResponseResult list(SensitiveDTO sensitiveDTO) {
//        1. 检测分页参数
        sensitiveDTO.checkParam();

//        2.根据名称模糊分页查询
        Page<AdSensitive> adSensitivePage = new Page<>(sensitiveDTO.getPage(), sensitiveDTO.getSize());

        LambdaQueryWrapper<AdSensitive> adSensitiveLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(sensitiveDTO.getName())) {
            adSensitiveLambdaQueryWrapper.like(AdSensitive::getSensitives, sensitiveDTO.getName());
        }

//        3. 调用MP进行查询
        IPage<AdSensitive> page = page(adSensitivePage, adSensitiveLambdaQueryWrapper);

//        4. 结果返回
        PageResponseResult pageResponseResult = new PageResponseResult(
                sensitiveDTO.getPage(),
                sensitiveDTO.getSize(),
                page.getTotal(),
                page.getRecords());

        return pageResponseResult;
    }

    /**
     * 新增敏感词
     * @param adSensitive
     * @return
     */
    @Override
    public ResponseResult insert(AdSensitive adSensitive) {
//        1. 检查参数
        if (adSensitive == null || StringUtils.isBlank(adSensitive.getSensitives())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"参数校验失败");
        }

//        2. 判断敏感词是否存在
        LambdaQueryWrapper<AdSensitive> adSensitiveLambdaQueryWrapper = new LambdaQueryWrapper<>();
        adSensitiveLambdaQueryWrapper.eq(AdSensitive::getSensitives, adSensitive.getSensitives());
        int count = count(adSensitiveLambdaQueryWrapper);
        if (count > 0) { // 已存在
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST,"敏感词已存在");
        }

//        3. 敏感词不存在,保存
        adSensitive.setCreateTime(new Date());
        save(adSensitive);

//        4. 成功新增
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 修改敏感词
     * @param adSensitive
     * @return
     */
    @Override
    public ResponseResult update(AdSensitive adSensitive) {
//        1. 检查参数
        if (adSensitive.getId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"敏感词ID不存在");
        }

//        2. 查询敏感词是否存在
        AdSensitive sensitive = getById(adSensitive.getId());
        if (sensitive == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST, "敏感词不存在");
        }

//        3. 如果敏感词不为空,并且新旧敏感词不同,再到数据库查询敏感词是否重复
        if (StringUtils.isNotBlank(adSensitive.getSensitives()) && !adSensitive.getSensitives().equals(sensitive.getSensitives())) {
            LambdaQueryWrapper<AdSensitive> adSensitiveLambdaQueryWrapper = new LambdaQueryWrapper<>();
            adSensitiveLambdaQueryWrapper.eq(AdSensitive::getSensitives, adSensitive.getSensitives());
            List<AdSensitive> adSensitiveList = list(adSensitiveLambdaQueryWrapper);
            if (adSensitiveList.size() > 0) {
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID, "敏感词重复");
            }
        }

//        4.更新
        updateById(adSensitive);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 删除敏感词
     * @param id
     * @return
     */
    @Override
    public ResponseResult delete(Integer id) {
//        1. 检查参数
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "敏感词id为空");
        }

//        2.查询敏感词是否存在
        AdSensitive sensitive = getById(id);
        if (sensitive == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "当前ID敏感词不存在,删除失败");
        }

//        3. 删除
        removeById(id);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

}
