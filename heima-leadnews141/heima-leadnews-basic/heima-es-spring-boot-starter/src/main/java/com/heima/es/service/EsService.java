package com.heima.es.service;

import com.heima.model.common.dtos.PageResponseResult;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;

public interface EsService<T> {
    void createIndex(String source,String indexName);
    boolean existIndex(String indexName);
    void deleteIndex(String indexName);
    void save(T t,String indexName);
    void saveBatch(List<T> list, String indexName);
    void deleteById(String id,String indexName);
    T getById(String id,Class<T> tClass,String indexName);
    PageResponseResult search(SearchSourceBuilder sourceBuilder, Class<T> tClass, String indexName);
}