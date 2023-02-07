package com.heima.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class GeneratePageServiceImpl implements GeneratePageService {

    @Autowired
    private Configuration configuration;

    @Resource(name = "minIOFileStorageService")
    private FileStorageService fileStorageService;

    @Value("${file.minio.prefix}")
    private String prefix;

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private AuthorMapper authorMapper;

    /**
     * 生成文章静态页
     */
    @Override
    public void generateArticlePage(String content, ApArticle apArticle) {
//        1. 获取文章内容
        try {
//        2. 获取freemarkder模板
            Template template = configuration.getTemplate("article.ftl");

//            3. 准备数据
            HashMap<String, Object> params = new HashMap<>();
//            文章详情
            params.put("content", JSON.parseArray(content, Map.class));
//            文章信息
            params.put("article", apArticle);

            ApAuthor author = authorMapper.selectById(apArticle.getAuthorId());
            if (author == null) {
                CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST, "对应的作者信息不存在");
            }
//            作者 对应的 apUserId
            params.put("authorApUserId",author.getUserId());

//            4. 使用数据替换模板中的内容 输出到 string
            StringWriter stringWriter = new StringWriter();
            template.process(params, stringWriter);
            String htmlStr = stringWriter.toString();

//            5. 封装输入流 字节数组输入流
            InputStream is = new ByteArrayInputStream(htmlStr.getBytes());

//            6. 生成页码吧html文件上传到minio中
            String path = fileStorageService.store(prefix, apArticle.getId() + ".html", "text/html", is);

//            7. 修改ap_article表,保存static_url字段
            apArticle.setStaticUrl(path);
            apArticleMapper.updateById(apArticle);
            log.info("文章详情静态页面生成成功 staticUrl========>{}", path);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文章静态页生成失败,请检查目标是否存在,========> articleId:{}=========> {}", apArticle.getId(), e.getCause());
            CustException.cust(AppHttpCodeEnum.SERVER_ERROR, "生成静态页失败");
        } catch (TemplateException e) {
            e.printStackTrace();
            log.error("文章静态页生成失败,请检查模板语法或变量是否正确========> articleId:{}=========> {}", apArticle.getId(), e.getCause());
            CustException.cust(AppHttpCodeEnum.SERVER_ERROR, "生成静态页失败");
        }
    }
}
