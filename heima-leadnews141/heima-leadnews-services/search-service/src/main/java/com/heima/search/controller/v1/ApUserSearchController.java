package com.heima.search.controller.v1;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.HistorySearchDTO;
import com.heima.model.search.dtos.UserSearchDTO;
import com.heima.search.service.ApUserSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * APP用户搜索信息表 前端控制器
 * @author itheima
 */
@Slf4j
@Api(value="搜索历史记录API",tags="搜索历史记录API")
@RestController
@RequestMapping("/api/v1/history")
public class ApUserSearchController{
    @Autowired
    private ApUserSearchService apUserSearchService;
    @ApiOperation("加载搜索历史记录")
    @PostMapping("/load")
    public ResponseResult findUserSearch(@RequestBody UserSearchDTO userSearchDto) {
        return apUserSearchService.findUserSearch(userSearchDto);
    }

    @ApiOperation("删除搜索历史记录")
    @PostMapping("/del")
    public ResponseResult delUserSearch(@RequestBody HistorySearchDTO historySearchDto) {
        return apUserSearchService.delUserSearch(historySearchDto);
    }
}