package com.heima.article.controller.v1;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.article.service.AuthorService;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author mrchen
 * @date 2022/4/22 15:41
 */
@Api("文章作者controller")
@RestController
@RequestMapping("/api/v1/author")
public class AuthorController {
    @Autowired
    AuthorService authorService;

    @ApiOperation("根据用户id查询作者信息")
    @GetMapping("/findByUserId/{userId}")
    public ResponseResult findByUserId(@PathVariable("userId") Integer userId){
        ApAuthor one = authorService.getOne(Wrappers.<ApAuthor>lambdaQuery().eq(ApAuthor::getUserId, userId));
        return ResponseResult.okResult(one);
    }
    @ApiOperation("保存作者信息")
    @PostMapping("/save")
    public ResponseResult save(@RequestBody ApAuthor apAuthor){
        authorService.save(apAuthor);
        return ResponseResult.okResult();
    }




}
