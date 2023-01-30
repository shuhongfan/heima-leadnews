package com.heima.search.controller.v1;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.UserSearchDTO;
import com.heima.search.service.ApAssociateWordsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Api(value="搜索联想词API",tags="搜索联想词API")
@Slf4j
@RestController
@RequestMapping("/api/v1/associate")
public class ApAssociateWordsController {
    @Autowired
    private ApAssociateWordsService apAssociateWordsService;
    @ApiOperation("搜索联想词")
    @PostMapping("/search")
    public ResponseResult findAssociate(@RequestBody UserSearchDTO userSearchDto) {
        return apAssociateWordsService.findAssociate(userSearchDto);
    }
}