package com.heima.wemedia.controller.v1;
import com.heima.model.common.constants.wemedia.WemediaConstants;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDTO;
import com.heima.wemedia.service.WmMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@Api(value = "素材管理API",tags = "素材管理API")
@RestController
@RequestMapping("/api/v1/material")
public class WmMaterialController {
    @Autowired
    private WmMaterialService materialService;
    @ApiOperation("上传素材")
    @PostMapping("/upload_picture")
    public ResponseResult uploadPicture(MultipartFile multipartFile) {
        return materialService.uploadPicture(multipartFile);
    }

    @ApiOperation("查询素材列表")
    @PostMapping("/list")
    public ResponseResult findList(@RequestBody WmMaterialDTO dto) {
        return materialService.findList(dto);
    }

    @ApiOperation(value = "删除素材",notes = "根据ID删除素材,有关联的素材不可以删除")
    @GetMapping("/del_picture/{id}")
    public ResponseResult delPicture(@PathVariable("id") Integer id) {
        return materialService.delPicture(id);
    }

    @ApiOperation("取消收藏素材")
    @GetMapping("/cancel_collect/{id}")
    public ResponseResult cancelCollectionMaterial(@PathVariable("id") Integer id) {
        return materialService.updateStatus(id, WemediaConstants.CANCEL_COLLECT_MATERIAL);
    }

    @ApiOperation("收藏素材")
    @GetMapping("/collect/{id}")
    public ResponseResult collectionMaterial(@PathVariable("id") Integer id) {
        return materialService.updateStatus(id, WemediaConstants.COLLECT_MATERIAL);
    }
}