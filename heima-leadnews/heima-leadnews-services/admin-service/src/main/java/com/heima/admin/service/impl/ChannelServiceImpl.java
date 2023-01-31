package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.admin.mapper.ChannelMapper;
import com.heima.admin.service.ChannelService;
import com.heima.common.exception.CustException;
import com.heima.common.exception.CustomException;
import com.heima.model.admin.dtos.ChannelDTO;
import com.heima.model.admin.pojo.AdChannel;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 频道管理
 */
@Service
public class ChannelServiceImpl extends ServiceImpl<ChannelMapper, AdChannel> implements ChannelService {
    @Override
    public ResponseResult findByNameAndPage(ChannelDTO dto) {
        // 1. 校验参数  非空    分页
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"参数错误");
        }
        dto.checkParam();
        // 2. 条件查询
        //     封装分页查询
        Page<AdChannel> pageReq = new Page<>(dto.getPage(),dto.getSize());
        //     封装条件参数   name   status     ord排序
        // 构建支持lambda表达式的查询条件
        LambdaQueryWrapper<AdChannel> wrapper = Wrappers.lambdaQuery();
        // name 如果不为空  则模糊查询
        if(StringUtils.isNotBlank(dto.getName())){
            wrapper.like(AdChannel::getName,dto.getName());
        }
        // status 如果不为空
        if(dto.getStatus()!=null){
            wrapper.eq(AdChannel::getStatus,dto.getStatus());
        }
        // 排序
        wrapper.orderByAsc(AdChannel::getOrd);
        // 3. 执行查询  封装返回结果   PageResponseResult
        IPage<AdChannel> pageResult = this.page(pageReq, wrapper);
        return new PageResponseResult(dto.getPage(),dto.getSize(),pageResult.getTotal(),pageResult.getRecords());
    }

    @Override
    public ResponseResult insert(AdChannel channel) {
        // 1. 参数校验  (name 不为空   不能大于10个字符  不能重复)
        String name = channel.getName();
        if (StringUtils.isBlank(name)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"频道名称不能为空");
        }
        if(name.length() > 10){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"频道名称长度不能大于10");
        }
        // 查询名称是否重复
        int count = this.count(Wrappers.<AdChannel>lambdaQuery().eq(AdChannel::getName, channel.getName()));
        if(count > 0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"频道名称重复");
        }
        // 2. 保存频道 (createTime 补全)
        channel.setCreatedTime(new Date());
        this.save(channel);
        // 3. 返回结果
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult update(AdChannel adChannel) {
        // 1. 校验参数  (id   )
        if (adChannel.getId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"频道id不能为空");
        }
        AdChannel oldChannel = this.getById(adChannel.getId());
        if (oldChannel == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"频道不存在");
        }
        //    如果 频道名称不为空  并且  新的频道名称 和 老的频道名称不一致  检查频道名称是否重复
        String name = adChannel.getName();
        if(StringUtils.isNotBlank(name)&&!name.equals(oldChannel.getName())){
            int count = this.count(Wrappers.<AdChannel>lambdaQuery().eq(AdChannel::getName, name));
            if(count > 0){
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"频道名称重复");
            }
        }
        // 2. 修改频道
        updateById(adChannel);
        // 3. 返回结果
        return ResponseResult.okResult();
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult deleteById(Integer id) {
        // 1. 校验参数
        if(id == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"频道id不能为空");
        }
        // 写
        AdChannel channel = this.getById(id);
        if(channel == null){
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST);
//            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"频道不存在");
        }
        if (channel.getStatus()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_ALLOW,"频道有效不允许删除");
        }
        // 删除频道
        this.removeById(id);
        // 还有一些业务逻辑
//        if(id > 45){
//            // 返回错误信息
////            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID,"业务参数校验失败，id不能大于45");
//            CustException.cust(AppHttpCodeEnum.PARAM_INVALID);
//        }
        return ResponseResult.okResult();
    }
}
