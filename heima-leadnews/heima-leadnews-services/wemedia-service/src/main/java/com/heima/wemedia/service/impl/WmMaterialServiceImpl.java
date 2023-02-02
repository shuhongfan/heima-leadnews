package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.wemedia.WemediaConstants;
import com.heima.common.exception.CustException;
import com.heima.file.service.FileStorageService;
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
import com.heima.wemedia.service.WmNewsMaterialService;
import io.seata.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Autowired
    private FileStorageService fileStorageService;

    @Value("${file.oss.prefix}")
    String preFix;

    @Value("${file.oss.web-site}")
    String webSite;

    @Autowired
    private WmNewsMaterialService wmNewsMaterialService;

    /**
     * 上传图片接口
     * @param multipartFile
     * @return
     */
    @Override
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
//        1. 参数校验 （ 文件对象 判断是否登录  文件后缀是否支持）
        if (multipartFile == null || multipartFile.getSize() <= 0) {
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"请上传正确的文件");
        }
        WmUser user = WmThreadLocalUtils.getUser();
        if (user == null) {
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN);
        }

//        获得原始文件名称
        String originalFilename = multipartFile.getOriginalFilename();
        if (!checkFileSuffix(originalFilename)) {
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"文件类型不支持,目前支持[JPG JGGE PNG GIF]");
        }

//        2. 上传到OSS （生成新的文件名称 上传到OSS）
        String filePath = null;
        try {
//            新文件名
            String newFileName = UUID.randomUUID().toString().replaceAll("-", "");

//            后缀
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            filePath = fileStorageService.store(preFix, newFileName + suffix, multipartFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            CustException.cust(AppHttpCodeEnum.SERVER_ERROR,"文件上传到阿里云失败");
        }

//        3. 保存到数据库
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setUserId(user.getId());
        wmMaterial.setUrl(filePath);
        wmMaterial.setType((short) 0);
        wmMaterial.setIsCollection((short) 0);
        wmMaterial.setCreatedTime(new Date());
        save(wmMaterial);

//        4. 封装返回
        wmMaterial.setUrl(webSite+wmMaterial.getUrl());
        return ResponseResult.okResult(wmMaterial);
    }

    /**
     * 素材列表查询
     * @param dto
     * @return
     */
    @Override
    public ResponseResult findList(WmMaterialDTO dto) {
//        1. 校验参数 分页 参数
        dto.checkParam();
        WmUser user = WmThreadLocalUtils.getUser();
        if (user == null) {
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN);
        }

//        2. 查询
//        2.1 分页条件
        Page<WmMaterial> pageReq = new Page<>(dto.getPage(),dto.getSize());

//        2.2 查询条件 （收藏条件 用户id 发布时间降序）
        LambdaQueryWrapper<WmMaterial> query = Wrappers.<WmMaterial>lambdaQuery();

//        查询自己上传的素材
        query.eq(WmMaterial::getUserId, user.getId());

//        按照发布时间进行降序排序
        query.orderByDesc(WmMaterial::getCreatedTime);

        if (WemediaConstants.COLLECT_MATERIAL.equals(dto.getIsCollection())) {
//            查询收藏的素材
            query.eq(WmMaterial::getIsCollection, dto.getIsCollection());
        }

//        3. 执行查询 封装分页结果返回 路径上+访问前缀
        IPage<WmMaterial> pageResult = page(pageReq, query);
        List<WmMaterial> records = pageResult.getRecords();
        for (WmMaterial record : records) {
            record.setUrl(webSite+record.getUrl());
        }
        return new PageResponseResult(dto.getPage(),dto.getSize(),pageResult.getTotal(),records);
    }

    /**
     * 删除图片
     * @param id
     * @return
     */
    @Override
    public ResponseResult delPicture(Integer id) {
//        1.参数校验
        if (id == null) {
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"图片id不能为空");
        }

//        2. 查询当前图片是否存在
        WmMaterial material = getById(id);
        if (material == null) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"当前图片不存在");
        }

//        3. 查询当前图片是否被文章引用
        Integer count = wmNewsMaterialService.countNewsBymaterialId(id);
        if (count > 0) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_ALLOW,"当前图片被文章引用不允许删除");
        }

//        4. 删除数据库文件
        removeById(id);

//        5. 删除OSS文件
        fileStorageService.delete(material.getUrl());

//        6.返回
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 收藏与取消收藏
     * @param id
     * @param type
     * @return
     */
    @Override
    public ResponseResult updateStatus(Integer id, Short type) {
//        1. 检查参数
        if (id == null || type == null) {
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID);
        }

//        2.检查id是否存在
        WmMaterial material = getById(id);
        if (material == null) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"图片不存在");
        }

//        3. 判断图片是否属于当前用户
        WmUser user = WmThreadLocalUtils.getUser();
        if (!material.getUserId().equals(user.getId())) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_ALLOW,"当前图片不属于此用户,无法收藏");
        }

        material.setType(type);
//        4. 更新
        updateById(material);

//        update(Wrappers.<WmMaterial>lambdaUpdate()  // 如果只想修改指定字段 可以使用此方法
//                .set(WmMaterial::getIsCollection,type)
//                .eq(WmMaterial::getId,id)
//                .eq(WmMaterial::getUserId,uid));

//        5. 返回
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 检查上传文件后缀
     * @param originalFilename
     * @return
     */
    private boolean checkFileSuffix(String originalFilename) {
        if (StringUtils.isNotBlank(originalFilename)) {
            List<String> allowSuffix = Arrays.asList(".jpg", ".jpeg", ".png", ".gif");
            for (String suffix : allowSuffix) {
                if (originalFilename.endsWith(suffix)) {
                    return true;
                }
            }
        }
        return false;
    }
}
