package com.heima.article.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * InitializingBean: springbean生命周期接口  代表完成bean装配后 执行的初始化方法
 * 这个类的目的：
 *     设置rabbitmq消息序列化机制  （默认jdk效率差）
 *     设置rabbitmq消息发送确认 回调
 *     设置rabbitmq消息返还 回调
 */
@Configuration
@Slf4j
public class RabbitConfig implements InitializingBean {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Override
    public void afterPropertiesSet()  {
        log.info("初始化rabbitMQ配置 ");
        // 设置消息转换器
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        // 设置发送确认 回调方法
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             * @param correlationData 对比数据
             * @param ack  是否成功发送到mq exchange
             * @param cause  原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (!ack){
                    // TODO 可扩展自动重试
                    log.error("发送消息到mq失败  ，原因: {}",cause);
                }
            }
        });
        // 设置消息返还 回调方法
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             * @param message  消息内容
             * @param replyCode  回复状态
             * @param replyText  回复文本提示
             * @param exchange   交换机
             * @param routingKey   路由
             */
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                // TODO 可扩展自动重试
                log.error("消息返还回调触发  ，交换机: {} , 路由: {} , 消息内容: {} , 原因: {}  ",exchange,routingKey,message,replyText);
            }
        });
    }
}