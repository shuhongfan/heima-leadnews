package com.heima.article.job;

import com.heima.article.service.HotArticleService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mrchen
 * @date 2022/5/6 9:33
 */
@Component
@Slf4j
public class ComputeHotArticleJob {
    @Autowired
    HotArticleService hotArticleService;


    @XxlJob("computeHotArticleJob")
    public ReturnT handle(String param){
        log.info("热文章分值计算调度任务开始执行....");
        // 待实现热点文章分值计算任务
        hotArticleService.computeHotArticle();
        log.info("热文章分值计算调度任务完成....");
        return ReturnT.SUCCESS;
    }
}
