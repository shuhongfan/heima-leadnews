package com.heima.datasync.service.impl;

import com.google.common.collect.Lists;
import com.heima.datasync.mapper.ApArticleMapper;
import com.heima.datasync.service.EsDataService;
import com.heima.es.service.EsService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.vos.SearchArticleVO;
import com.heima.model.common.constants.search.SearchConstants;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mrchen
 * @date 2022/5/8 10:39
 */
@Service
public class EsDataServiceImpl implements EsDataService {


    @Autowired
    private EsService<SearchArticleVO> esService;

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Override
    public ResponseResult dataInit() {
        // 1. 判断当前 指定的索引库是否存在
        boolean exist = esService.existIndex(SearchConstants.ARTICLE_INDEX_NAME);
        // 2. 如果存在  直接删除对应的索引库
        if(exist){
            esService.deleteIndex(SearchConstants.ARTICLE_INDEX_NAME);
        }
        // 3. 新建索引库
        esService.createIndex(SearchConstants.ARTICLE_INDEX_MAPPING,SearchConstants.ARTICLE_INDEX_NAME);
        // 4. 从数据库中 查询文章数据
        List<ApArticle> allArticles = apArticleMapper.findAllArticles();

        // 5. 批量的保存到es索引库中
        List<SearchArticleVO> articleVOList = allArticles.stream()
                .map(apArticle -> {
                    SearchArticleVO articleVO = new SearchArticleVO();
                    BeanUtils.copyProperties(apArticle, articleVO);
                    return articleVO;
                }).collect(Collectors.toList());
        esService.saveBatch(articleVOList,SearchConstants.ARTICLE_INDEX_NAME);
        return ResponseResult.okResult();
    }
}
