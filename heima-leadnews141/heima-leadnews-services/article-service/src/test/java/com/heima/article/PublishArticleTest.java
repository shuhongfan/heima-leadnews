package com.heima.article;

import com.heima.article.service.ApArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author mrchen
 * @date 2022/4/30 11:11
 */
@SpringBootTest
//@RunWith(SpringRunner.class)
public class PublishArticleTest {
    @Autowired
    ApArticleService apArticleService;
    @Test
    public void publishArticle(){
        apArticleService.publishArticle(6270);
    }
}
