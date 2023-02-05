package com.heima.wemedia.listen;

import com.heima.common.constants.message.NewsAutoScanConstants;
import com.heima.wemedia.service.WmNewsAutoScanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WemediaNewsAutoListener {
    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;

    /**
     * queues: 监听指定队列
     * queuesToDeclare: 声明并监听指定队列
     * bindings: 声明队列  交换机  并通过路由绑定
     */
    @RabbitListener(queuesToDeclare = @Queue(NewsAutoScanConstants.WM_NEWS_AUTO_SCAN_QUEUE))
    public void handlerAutoScanMsg(String newsId) {
        log.info("接收到自动审核消息 文章id:{} 自动审核开始===>", newsId);
        wmNewsAutoScanService.autoScanWmNews(Integer.valueOf(newsId));
        log.info("接收到自动审核消息 文章id:{} 自动审核完毕===>", newsId);
    }
}
