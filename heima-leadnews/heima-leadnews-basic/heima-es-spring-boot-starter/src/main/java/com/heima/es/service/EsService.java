package com.heima.es.service;

import com.heima.model.common.dtos.PageResponseResult;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;

public interface EsService<T> {
    /**
     * 创建索引
     * @param source
     * @param indexName
     */
    void createIndex(String source,String indexName);


    /**
     * 判断索引是否存在
     * @param indexName
     * @return
     */
    boolean existIndex(String indexName);

    /**
     * 删除索引
     * @param indexName
     */
    void deleteIndex(String indexName);

    /**
     * 文档的新增
     * @param t
     * @param indexName
     */
    void save(T t,String indexName);

    /**
     * 文档批量新增
     * @param list
     * @param indexName
     */
    void saveBatch(List<T> list, String indexName);

    /**
     * 删除文档
     * @param id
     * @param indexName
     */
    void deleteById(String id,String indexName);

    /**
     * 根据id查找文档
     * @param id
     * @param tClass
     * @param indexName
     * @return
     */
    T getById(String id,Class<T> tClass,String indexName);

    /**
     * 查询并返回结果，并且带高亮、分页、排序
     * @param sourceBuilder 条件
     * @return 分页结果
     */
    PageResponseResult search(SearchSourceBuilder sourceBuilder, Class<T> tClass, String indexName);
}