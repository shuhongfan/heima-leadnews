package com.heima.article.listen;

import com.heima.model.common.constants.article.HotArticleConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author mrchen
 * @date 2022/5/6 11:21
 */
@Component
@Slf4j
public class HotArticleScoreListener {
    @Autowired
    StringRedisTemplate redisTemplate;
    @RabbitListener(queuesToDeclare = @Queue(HotArticleConstants.HOT_ARTICLE_SCORE_BEHAVIOR_QUEUE))
    public void listenNewBehaviorHandler(String newBehavior){
        log.info(" 接收到 新产生的文章行为数据: {}",newBehavior);
        try {
            redisTemplate.opsForList().rightPush(HotArticleConstants.HOT_ARTICLE_SCORE_BEHAVIOR_LIST,newBehavior);
            log.info(" 接收到 新产生的文章行为数据 存入redis list成功 ");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(" 新行为 存入redis失败,  失败原因: {}",e.getMessage());
        }
    }
}
