package com.heima.search.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.UserSearchDTO;
import com.heima.model.search.vos.SearchArticleVO;

public interface ArticleSearchService {
    /**
     ES文章分页搜索
     @return
     */
    ResponseResult search(UserSearchDTO userSearchDto);

    /**
     * 添加索引文章
     * @param article
     */
    void saveArticle(SearchArticleVO article);

    /**
     * 删除索引文章
     * @param articleId
     */
    void deleteArticle(String articleId);
}
