package com.heima.article.controller.v1;

import com.heima.article.service.ApArticleService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.vos.SearchArticleVO;
import com.heima.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping("/api/v1/article")
public class ApArticleController {
    @Autowired
    ApArticleService apArticleService;
    @ApiOperation("根据ID查询文章搜索VO")
    @GetMapping("{id}")
    public ResponseResult<SearchArticleVO> findArticle(@PathVariable Long id) {
        SearchArticleVO searchArticleVo = null;
        ApArticle article = apArticleService.getById(id);
        if(article!=null){
            searchArticleVo = new SearchArticleVO();
            BeanUtils.copyProperties(article,searchArticleVo);
        }
        return ResponseResult.okResult(searchArticleVo);
    }
}