package com.heima.aritcle;

import com.heima.article.ArticleApplication;
import com.heima.article.service.ApArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ArticleApplication.class)
public class PublishArticleTest {
    @Autowired
    private ApArticleService apArticleService;

    @Test
    public void publishArticle() {
        apArticleService.publishArticle(6278);
    }

}
