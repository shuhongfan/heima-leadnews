package com.heima.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.mapper.AuthorMapper;
import com.heima.article.service.GeneratePageService;
import com.heima.common.exception.CustException;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.enums.AppHttpCodeEnum;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mrchen
 * @date 2022/5/2 11:21
 */
@Service
@Slf4j
public class GeneratePageServiceImpl implements GeneratePageService {
    @Autowired
    private Configuration configuration;
    @Autowired
    private AuthorMapper authorMapper;
    @Autowired
    @Qualifier("minIOFileStorageService")
    private FileStorageService fileStorageService;
    @Value("${file.minio.prefix}")
    private String prefix;
    @Value("${file.minio.readPath}")
    private String readPath;
    @Autowired
    private ApArticleMapper apArticleMapper;
    @Override
    public void generateArticlePage(String content, ApArticle apArticle) {
        try {
            // 1. 获取freemarker 模板
            Template template = configuration.getTemplate("article.ftl");
            // 2. 准备数据模型   authorApUserId  article   content
            Map params = new HashMap<>();
            // 文章 对象
            params.put("article",apArticle);
            // 文章 内容                 List<Map>
            params.put("content", JSON.parseArray(content,Map.class));
            // 作者对应的 ap_user_id   apArticle ==> ap_author_id ==> user_id
            ApAuthor apAuthor = authorMapper.selectById(apArticle.getAuthorId());
            if (apAuthor == null) {
                CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"对应的作者信息不存在");
            }
            params.put("authorApUserId",apAuthor.getUserId());
            // 3.  使用数据 替换 模板  == 输出到  StringWriter
            StringWriter stringWriter = new StringWriter();
            template.process(params,stringWriter);
            String htmlStr = stringWriter.toString();
            // 4.  封装输入流  字节数组输入流 ( 字符串.getBytes() )
            InputStream inputStream = new ByteArrayInputStream(htmlStr.getBytes());
            // 5. 将静态页内容上传到 minio中
            String path = fileStorageService.store(prefix, apArticle.getId() + ".html", "text/html", inputStream);
            // 6. 修改文章static_url 静态页路径
            apArticle.setStaticUrl(path);
            apArticleMapper.updateById(apArticle);
            log.info("生成 文章静态页 成功:   访问地址: {}", readPath + path);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("生成静态页失败  原因: {}   请检查模板是否存在",e.getMessage());
            CustException.cust(AppHttpCodeEnum.SERVER_ERROR,"生成静态页失败");
        } catch (TemplateException e) {
            e.printStackTrace();
            log.error("生成静态页失败  原因: {}   请检查模板语法 或 变量是否正确",e.getMessage());
            CustException.cust(AppHttpCodeEnum.SERVER_ERROR,"生成静态页失败");
        }
    }
}
