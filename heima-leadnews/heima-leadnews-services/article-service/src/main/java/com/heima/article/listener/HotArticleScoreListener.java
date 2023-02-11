package com.heima.article.listener;

import com.heima.common.constants.article.HotArticleConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HotArticleScoreListener {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @RabbitListener(queuesToDeclare = @Queue(value = HotArticleConstants.HOT_ARTICLE_SCORE_BEHAVIOR_QUEUE))
    public void listenNewBehaviorHandler(String newBehavior) {
        log.info("接收到 新产生的文章行为数据：{}", newBehavior);

        try {
            redisTemplate.opsForList().rightPush(HotArticleConstants.HOT_ARTICLE_SCORE_BEHAVIOR_LIST, newBehavior);
            log.info("接收到 新产生的文章行为数据 存入redis list成功");
        } catch (Exception e) {
            log.info("新行为 存入redis 失败，失败原因：{}", e.getMessage());
        }

    }

}
