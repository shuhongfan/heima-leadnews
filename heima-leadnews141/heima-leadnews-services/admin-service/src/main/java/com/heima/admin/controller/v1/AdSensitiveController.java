package com.heima.admin.controller.v1;
import com.heima.admin.service.AdSensitiveService;
import com.heima.model.admin.dtos.SensitiveDTO;
import com.heima.model.admin.pojo.AdSensitive;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@Api(value = "敏感词管理API",tags = "敏感词管理API")
@RestController
@RequestMapping("/api/v1/sensitive")
public class AdSensitiveController{
    @Autowired
    AdSensitiveService sensitiveService;
  
    @ApiOperation(value = "敏感词查询")
    @PostMapping("/list")
    public ResponseResult list(@RequestBody SensitiveDTO dto) {
        return sensitiveService.list(dto);
    }
  
    @ApiOperation("敏感词添加")
    @PostMapping("/save")
    public ResponseResult insert(@RequestBody AdSensitive adSensitive) {
        return sensitiveService.insert(adSensitive);
    }
  
    @ApiOperation("敏感词修改")
    @PostMapping("/update")
    public ResponseResult update(@RequestBody AdSensitive adSensitive) {
        return sensitiveService.update(adSensitive);
    }
  
    @ApiOperation("敏感词删除")
    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable("id") Integer id) {
        return sensitiveService.delete(id);
    }

    @ApiOperation(value = "查询敏感词内容list")
    @PostMapping("/sensitives")
    public ResponseResult sensitives() {
        return sensitiveService.selectAllSensitives();
    }
}