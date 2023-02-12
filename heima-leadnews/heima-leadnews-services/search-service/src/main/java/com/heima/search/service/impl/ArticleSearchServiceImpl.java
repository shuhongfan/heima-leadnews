package com.heima.search.service.impl;

import com.heima.common.constants.search.SearchConstants;
import com.heima.common.exception.CustException;
import com.heima.es.service.EsService;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.search.dtos.UserSearchDTO;
import com.heima.model.search.vos.SearchArticleVO;
import com.heima.model.threadlocal.AppThreadLocalUtils;
import com.heima.model.user.pojos.ApUser;
import com.heima.search.service.ApUserSearchService;
import com.heima.search.service.ArticleSearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleSearchServiceImpl implements ArticleSearchService {
    @Value("${file.minio.readPath}")
    private String readPath;

    @Value("${file.oss.web-site}")
    private String webSite;

    @Autowired
    private EsService esService;

    @Autowired
    private ApUserSearchService apUserSearchService;

    /**
     * ES文章分页搜索
     * @param dto
     * @return
     */
    @Override
    public ResponseResult search(UserSearchDTO dto) {
//        1.校验参数 （ 关键字 ）
        String searchWords = dto.getSearchWords();
        if (StringUtils.isBlank(searchWords)) {
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID, "搜素条件内容不能为空");
        }
        if (dto.getMinBehotTime() == null) {
            dto.setMinBehotTime(new Date());
        }

//        记录搜索历史记录
        ApUser user = AppThreadLocalUtils.getUser();
        if (user == null) {
            dto.setEntryId(user.getId());
        }

//        异步调用保存用户输入关键词记录
        apUserSearchService.insert(dto);

//        2. 构建搜索请求对象 SearchSourceBuilder
        SearchSourceBuilder builder = new SearchSourceBuilder();

//        2.1 构建查询条件 builder.query
        //        2.1.1 创建布尔条件  bool
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //        2.1.2 创建分词查询条件 match查询title ===》 must 加入bool
        boolQueryBuilder.must(QueryBuilders.matchQuery("title", searchWords));

        //        2.1.3 创建范围查询条件 range 查询 publishTime ==> filter 加入 bool
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("publishTime").lt(dto.getMinBehotTime()));
        builder.query(boolQueryBuilder);

//        2.2 设置高亮字段 builder.highlight
        HighlightBuilder highlightBuilder = new HighlightBuilder();

//                2.2.1 哪个字段高亮
        highlightBuilder.field("title");

//                2.2.2 前置标签
        highlightBuilder.field("<span>");
//                2.2.2 后置标签
        highlightBuilder.field("</span>");

        builder.highlighter(highlightBuilder);

//        2.3 构建排序条件 builder.sort 发布时间排序 （默认： score）
        builder.sort("publishTime", SortOrder.DESC);

//        2.4 构建分页条件吧builder.from(0) builder.size(pageSize)
        builder.size(dto.getPageSize());

//        3. 执行搜素，并封装返回结果
        PageResponseResult result = esService.search(builder, SearchArticleVO.class, SearchConstants.ARTICLE_INDEX_NAME);
        List<SearchArticleVO> list = (List<SearchArticleVO>) result.getData();

        if (CollectionUtils.isNotEmpty(list)) {
            for (SearchArticleVO searchArticleVO : list) {
//           3.1 staticUrl 要添加前缀
                searchArticleVO.setStaticUrl(readPath + searchArticleVO.getStaticUrl());

//           3.2 images 要添加前缀 webSite+url,url2
                String images = searchArticleVO.getImages();
                if (StringUtils.isNotBlank(images)) {
                    images = Arrays.stream(images.split(","))
                            .map(url -> webSite + url)
                            .collect(Collectors.joining(","));
                    searchArticleVO.setImages(images);

                }
            }
        }
        return result;
    }

    /**
     * 添加索引文章
     * @param article
     */
    @Override
    public void saveArticle(SearchArticleVO article) {
        esService.save(article, SearchConstants.ARTICLE_INDEX_NAME);
    }

    /**
     * 删除索引文章
     * @param articleId
     */
    @Override
    public void deleteArticle(String articleId) {
        esService.deleteById(articleId, SearchConstants.ARTICLE_INDEX_NAME);
    }
}
