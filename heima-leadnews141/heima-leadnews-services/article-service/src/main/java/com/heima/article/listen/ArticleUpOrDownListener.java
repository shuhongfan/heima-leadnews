package com.heima.article.listen;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.article.service.ApArticleConfigService;
import com.heima.model.article.pojos.ApArticleConfig;
import com.heima.model.common.constants.message.NewsUpOrDownConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mrchen
 * @date 2022/4/30 15:46
 */
@Component
@Slf4j
public class ArticleUpOrDownListener {
    @Autowired
    ApArticleConfigService apArticleConfigService;


    @RabbitListener(queues = NewsUpOrDownConstants.NEWS_UP_FOR_ARTICLE_CONFIG_QUEUE)
    public void handleNewsUpMsg(String articleId){
        log.info("接收到文章  上架消息   文章id: {}",articleId);
        apArticleConfigService.update(
                Wrappers.<ApArticleConfig>lambdaUpdate()
                        .set(ApArticleConfig::getIsDown,false)
                        .eq(ApArticleConfig::getArticleId,articleId)
        );
    }
    @RabbitListener(queues = NewsUpOrDownConstants.NEWS_DOWN_FOR_ARTICLE_CONFIG_QUEUE)
    public void handleNewsDownMsg(String articleId){
        log.info("接收到文章  下架消息   文章id: {}",articleId);
        apArticleConfigService.update(
                Wrappers.<ApArticleConfig>lambdaUpdate()
                        .set(ApArticleConfig::getIsDown,true)
                        .eq(ApArticleConfig::getArticleId,articleId)
        );
    }
}
