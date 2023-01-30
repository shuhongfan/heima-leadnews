package com.heima.wemedia.listen;

import com.heima.model.common.constants.message.NewsAutoScanConstants;
import com.heima.wemedia.service.WmNewsAutoScanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mrchen
 * @date 2022/4/28 15:48
 */
@Component
@Slf4j
public class WemediaNewsAutoListener {
    @Autowired
    WmNewsAutoScanService wmNewsAutoScanService;
    /**
     * queues : 监听队列  不会自动创建队列
     *
     * queuesToDeclare ： 监听队列  不存在会自动创建队列
     *
     * bindings : 监听队列    可以创建  交换机  队列  绑定关系
     */
    @RabbitListener(queuesToDeclare = @Queue(NewsAutoScanConstants.WM_NEWS_AUTO_SCAN_QUEUE))
    public void handleAutoScanMsg(String newsId){
        log.info("接收到自动审核消息   文章id: {}   自动审核开始====>",newsId);
        wmNewsAutoScanService.autoScanWmNews(Integer.valueOf(newsId));
        log.info("                                自动审核执行完毕====>",newsId);
    }
}
