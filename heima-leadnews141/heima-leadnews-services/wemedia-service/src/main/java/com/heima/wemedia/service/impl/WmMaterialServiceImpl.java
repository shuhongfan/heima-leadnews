package com.heima.wemedia.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.exception.CustException;
import com.heima.file.service.FileStorageService;
import com.heima.model.common.constants.wemedia.WemediaConstants;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.threadlocal.WmThreadLocalUtils;
import com.heima.model.wemedia.dtos.WmMaterialDTO;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.model.wemedia.pojos.WmNewsMaterial;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.mapper.WmNewsMaterialMapper;
import com.heima.wemedia.service.WmMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author mrchen
 * @date 2022/4/24 16:59
 */
@Service
@Slf4j
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {
    @Autowired
    FileStorageService fileStorageService;
    @Value("${file.oss.prefix}")
    String prefix;
    @Value("${file.oss.web-site}")
    String webSite;
    /**
     * 上传素材
     * @param multipartFile
     * @return
     */
    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        // 1. 参数校验  (文件对象   判断是否登陆   文件后缀是否支持)
        if (multipartFile == null || multipartFile.getSize() <=0) {
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"请上传正确的文件");
        }
        WmUser user = WmThreadLocalUtils.getUser();
        if (user == null) {
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN);
        }
        // 获得原始的文件名称    [jpg  jpeg   png   gif]
        String originalFilename = multipartFile.getOriginalFilename();
        if(!checkFileSuffix(originalFilename)){
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"文件类型不支持， 目前支持:[jpg  jpeg   png   gif] ");
        }
        // 2. 上传到OSS ( 生成新的文件名称    上传到oss )
        String filePath = null;
        try {
            String newFileName = UUID.randomUUID().toString().replaceAll("-","");
            // mein.v004.jpg
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
             filePath = fileStorageService.store(prefix, newFileName + suffix, multipartFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            CustException.cust(AppHttpCodeEnum.SERVER_ERROR,"上传文件到阿里云失败");
        }
        // 3. 保存到数据库
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUserId(user.getId());
        wmMaterial.setUrl(filePath);
        wmMaterial.setType((short)0);
        wmMaterial.setIsCollection((short)0);
        wmMaterial.setCreatedTime(new Date());
        this.save(wmMaterial);
        // 4. 封装返回  需要将url的路径补全  用于前端回显图片
        wmMaterial.setUrl(webSite + wmMaterial.getUrl());
        return ResponseResult.okResult(wmMaterial);
    }

    @Override
    public ResponseResult findList(WmMaterialDTO dto) {
        // 1. 校验参数    分页    登陆
        dto.checkParam();
        WmUser user = WmThreadLocalUtils.getUser();
        if(user == null){
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN);
        }
        // 2. 查询
        // 2.1   分页条件
        Page<WmMaterial> pageReq = new Page<>(dto.getPage(),dto.getSize());
        // 2.2   查询条件   (收藏条件   用户id    发布时间降序)
        LambdaQueryWrapper<WmMaterial> queryWrapper = Wrappers.<WmMaterial>lambdaQuery();
        if (WemediaConstants.COLLECT_MATERIAL.equals(dto.getIsCollection())) {
            // 查询收藏的素材
            queryWrapper.eq(WmMaterial::getIsCollection,dto.getIsCollection());
        }
        // 查询自己上传的素材
        queryWrapper.eq(WmMaterial::getUserId,user.getId());
        // 按照发布时间降序排序
        queryWrapper.orderByDesc(WmMaterial::getCreatedTime);
        // 3. 执行查询  封装分页返回结果    路径上 + 访问前缀
        IPage<WmMaterial> pageResult = this.page(pageReq, queryWrapper);
        List<WmMaterial> records = pageResult.getRecords();
        for (WmMaterial record : records) {
            record.setUrl(webSite + record.getUrl());
        }
        return new PageResponseResult(dto.getPage(),dto.getSize(),pageResult.getTotal(),records);
    }

    @Autowired
    WmNewsMaterialMapper wmNewsMaterialMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult delPicture(Integer id) {
        // 1. 校验参数
        if(id == null){
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"id不能为空");
        }
        // 2. 根据id 查询出素材
        WmMaterial material = this.getById(id);
        if(material == null){
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"素材不存在");
        }
        // 3. 查询素材是否被引用
        Integer count = wmNewsMaterialMapper.selectCount(Wrappers.<WmNewsMaterial>lambdaQuery().eq(WmNewsMaterial::getMaterialId, id));
        if(count>0){
            CustException.cust(AppHttpCodeEnum.DATA_NOT_ALLOW,"素材被引用 ，无法删除");
        }
        // 4. 删除素材
        this.removeById(id);
        // 5. 删除OSS中对应的图片
        fileStorageService.delete(material.getUrl());
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateStatus(Integer id, Short type) {
        if(id == null){
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"id不能为空");
        }
        this.update(Wrappers.<WmMaterial>lambdaUpdate()
                .set(WmMaterial::getIsCollection,type)
                .eq(WmMaterial::getId,id)
                .eq(WmMaterial::getUserId,WmThreadLocalUtils.getUser().getId()));
        return ResponseResult.okResult();
    }

    /**
     * 检查文件后缀 是否支持
     * @param originalFilename
     * @return
     */
    private boolean checkFileSuffix(String originalFilename) {
        if (StringUtils.isNotBlank(originalFilename)) {
            List<String> allowSuffix = Arrays.asList(".jpg", ".jpeg", ".png", ".gif");
            for (String suffix : allowSuffix) {
                if(originalFilename.endsWith(suffix)){
                    return true;
                }
            }
        }
        return false;
    }
}
