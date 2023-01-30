package com.heima.admin.controller.v1;

import com.heima.admin.service.ChannelService;
import com.heima.model.admin.dtos.ChannelDTO;
import com.heima.model.admin.pojo.AdChannel;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author mrchen
 * @date 2022/4/21 11:45
 */
@Api(value = "频道管理controller",tags = "频道管理controller")
@RestController // @Controller    @ResponseBody ==> json
@RequestMapping("/api/v1/channel")
public class ChannelController {
    @Autowired
    private ChannelService channelService;

    @ApiOperation(value = "根据条件分页查询频道信息",notes = "条件 频道名称 状态 以ord升序查询频道")
    @ApiImplicitParams(
            @ApiImplicitParam(value = "dto",dataType = "ChannelDTO",required = true)
    )
    @PostMapping("/list")
    public ResponseResult list(@RequestBody ChannelDTO dto){
        return channelService.findByNameAndPage(dto);
    }
    @ApiOperation(value = "保存频道信息")
    @PostMapping("/save")
    public ResponseResult save(@RequestBody AdChannel channel){
        return channelService.insert(channel);
    }

    @ApiOperation(value = "修改频道信息")
    @PostMapping("/update")
    public ResponseResult update(@RequestBody AdChannel channel){
        return channelService.update(channel);
    }

    @ApiOperation(value = "删除频道信息")
    @GetMapping("/del/{id}")
    public ResponseResult delete(@PathVariable("id") Integer id){
        return channelService.deleteById(id);
    }
}
