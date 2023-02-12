package com.heima.search.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.HistorySearchDTO;
import com.heima.model.search.dtos.UserSearchDTO;

public interface ApUserSearchService {
    /**
     * 保存用户搜索历史记录
     * @param userSearchDto
     */
    public void insert( UserSearchDTO userSearchDto);

    /**
     查询搜索历史
     @param userSearchDto
     @return
     */
    ResponseResult findUserSearch(UserSearchDTO userSearchDto);


    /**
     删除搜索历史
     @param historySearchDto
     @return
     */
    ResponseResult delUserSearch(HistorySearchDTO historySearchDto);
}
