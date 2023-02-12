package com.heima.datasync.controller;

import com.heima.datasync.service.EsDataService;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value="同步数据API",tags="同步数据API")
@RestController
@RequestMapping("esdata")
public class DataSyncController {
    @Autowired
    private EsDataService esDataService;

    @ApiOperation(value="全量索引数据同步",notes="查询所有文章数据库数据，导入到es索引库中")
    @GetMapping("init")
    public ResponseResult esDataInit(){
        return esDataService.dataInit();
    }
}