package com.heima.file.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @Description 文件上传接口
 */
public interface FileStorageService {

    /**
     * @Description 文件上传
     * @param prefix 文件上传前缀
     * @param filename 文件名称
     * @param inputStream 文件流
     * @return pathUrl 全路径
     */
    String store(String prefix, String filename, InputStream inputStream);


    /**
     * @Description 文件上传
     * @param prefix 文件上传前缀
     * @param filename 文件名称
     * @param contentType 文件类型 "image/jpg" 或"text/html"
     * @param inputStream 文件流
     * @return pathUrl 全路径
     */
    String store(String prefix, String filename, String contentType, InputStream inputStream);

    /**
     * @Description 文件删除
     * @param pathUrl 全路径
     * @throws Exception
     */
    void delete(String pathUrl);


    /**
     * @Description 批量文件删除
     * @param pathUrls 全路径集合
     * @throws Exception
     */
    void deleteBatch(List<String> pathUrls);

    /**
     * @Description  下载文件
     * @param pathUrl 全路径
     * @return
     */
    InputStream downloadFile(String pathUrl);

    /**
     * @Description 获取文件文本内容
     * @param pathUrl 全路径
     * @return
     * @throws IOException
     */
    String getFileContent(String pathUrl) throws IOException;

}
