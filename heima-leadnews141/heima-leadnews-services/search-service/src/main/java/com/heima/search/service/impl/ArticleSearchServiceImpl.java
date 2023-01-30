package com.heima.search.service.impl;

import com.heima.common.exception.CustException;
import com.heima.es.service.EsService;
import com.heima.model.article.vos.SearchArticleVO;
import com.heima.model.common.constants.search.SearchConstants;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.search.dtos.UserSearchDTO;
import com.heima.model.threadlocal.AppThreadLocalUtils;
import com.heima.model.user.pojos.ApUser;
import com.heima.search.service.ApUserSearchService;
import com.heima.search.service.ArticleSearchService;
import org.apache.commons.lang3.StringUtils;
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

/**
 * @author mrchen
 * @date 2022/5/8 11:15
 */
@Service
public class ArticleSearchServiceImpl implements ArticleSearchService {
    @Autowired
    private EsService<SearchArticleVO> esService;

    @Value("${file.minio.readPath}")
    private String readPath;
    @Value("${file.oss.web-site}")
    private String webSite;
    @Autowired
    private ApUserSearchService apUserSearchService;
    @Override
    public ResponseResult search(UserSearchDTO dto) {
        // 1. 校验参数  (关键字)
        String searchWords = dto.getSearchWords();
        if (StringUtils.isBlank(searchWords)){
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"搜索条件内容不能为空");
        }
        if (dto.getMinBehotTime() == null) {
            dto.setMinBehotTime(new Date());
        }

        // 登陆用户id
        ApUser user = AppThreadLocalUtils.getUser();
        // 在异步方法中，无法获取当前线程中的用户信息
        dto.setLoginUserId(user==null?null:user.getId());
        apUserSearchService.insert(dto);



        // 2. 构建搜索请求对象  SearchSourceBuilder
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 2.1 构建查询条件  builder.query
            // 2.1.1 创建布尔条件  bool
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            // 2.1.2 创建分词查询条件  match查询title  ==> must 加入bool
        boolQuery.must(QueryBuilders.matchQuery("title",searchWords));
            // 2.1.3 创建范围查询条件  range查询publishTime  ==> filter 加入bool
        boolQuery.filter(QueryBuilders.rangeQuery("publishTime").lt(dto.getMinBehotTime()));
        builder.query(boolQuery);
        // 2.2 构建高亮条件  builder.highlight
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        // 2.2.1  哪个字段高亮
        highlightBuilder.field("title");
            // 2.2.2  前置标签
        highlightBuilder.preTags("<font style='color: red; font-size: inherit;'>");
            // 2.2.3  后置标签
        highlightBuilder.postTags("</font>");
        builder.highlighter(highlightBuilder);
        // 2.3 构建排序条件  builder.sort   发布时间降序 (默认: score)
        builder.sort("publishTime", SortOrder.DESC);
        // 2.4 构建分页条件  builder.from(0)  builder.size(pageSize)
        builder.size(dto.getPageSize());
        // 3 执行搜索，并封装返回结果
        PageResponseResult result = esService.search(builder, SearchArticleVO.class, SearchConstants.ARTICLE_INDEX_NAME);
        List<SearchArticleVO> list = (List<SearchArticleVO>)result.getData();
        for (SearchArticleVO articleVO : list) {
            // 3.1  staticUrl要添加前缀
            articleVO.setStaticUrl(readPath + articleVO.getStaticUrl());

            // 3.2  images 封面 要添加前缀   webSite + url1    webSite + url2,url3
            String images = articleVO.getImages();
            if (StringUtils.isNotBlank(images)) {
                images = Arrays.stream(images.split(","))
                        .map(url -> webSite + url)
                        .collect(Collectors.joining(","));
                articleVO.setImages(images);
            }
        }
        return result;
    }
    @Override
    public void saveArticle(SearchArticleVO article) {
        esService.save(article,SearchConstants.ARTICLE_INDEX_NAME);
    }
    @Override
    public void deleteArticle(String articleId) {
        esService.deleteById(articleId,SearchConstants.ARTICLE_INDEX_NAME);
    }
}
