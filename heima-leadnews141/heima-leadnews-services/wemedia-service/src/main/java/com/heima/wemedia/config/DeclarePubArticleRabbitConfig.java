package com.heima.wemedia.config;

import com.heima.model.common.constants.message.PublishArticleConstants;
import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置 延迟交换机   队列  和 绑定关系
 * web页面  控制台
 *
 * @Configuration  + @Bean   配置
 *
 * @RabbitListener (binding = )
 * @author mrchen
 * @date 2022/4/30 14:35
 */
@Configuration
public class DeclarePubArticleRabbitConfig {
    @Bean
    public DirectExchange delayDirect(){
        return ExchangeBuilder.directExchange(PublishArticleConstants.DELAY_DIRECT_EXCHANGE)
                              .delayed()  // 设置延迟交换机
                              .durable(true)
                              .build();
    }
    @Bean
    public Queue delayQueue(){
        return QueueBuilder.durable(PublishArticleConstants.PUBLISH_ARTICLE_QUEUE).build();
    }
    @Bean
    public Binding binding(){
        return BindingBuilder.bind(delayQueue()).to(delayDirect()).with(PublishArticleConstants.PUBLISH_ARTICLE_ROUTE_KEY);
    }


}
