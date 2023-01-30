package com.heima.article.listen;

import com.heima.article.service.ApArticleService;
import com.heima.model.common.constants.message.PublishArticleConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author mrchen
 * @date 2022/4/30 15:04
 */
@Component
@Slf4j
public class PublishArticleListener {
    @Autowired
    ApArticleService apArticleService;

    /**
     * queue  只监听队列
     *
     * queueToDeclera   监听队列   不存在 会创建
     *
     * bindings    监听队列  不存在  会创建  (队列   交换机   绑定关系)
     * @param newsId
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = PublishArticleConstants.DELAY_DIRECT_EXCHANGE,type = "direct",delayed = "true"),
            value = @Queue(value = PublishArticleConstants.PUBLISH_ARTICLE_QUEUE),
            key = PublishArticleConstants.PUBLISH_ARTICLE_ROUTE_KEY
    ))
    public void handlePublishMsg(String newsId){
        log.info("成功接收到发布文章消息  文章id:{}  当前时间: {}",newsId, LocalDateTime.now());
        try {
            apArticleService.publishArticle(Integer.valueOf(newsId));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发布文章失败，  原因: {}",e.getMessage());
        }
    }
}
