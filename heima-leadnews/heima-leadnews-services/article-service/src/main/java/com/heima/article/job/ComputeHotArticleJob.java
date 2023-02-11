package com.heima.article.job;

import com.heima.article.service.HotArticleService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ComputeHotArticleJob {

    @Autowired
    private HotArticleService hotArticleService;

    @XxlJob("ComputeHotArticleJob")
    public ReturnT handle(String param) {
        log.info("热点文章分值计算调度任务开始执行");

        hotArticleService.computeHotArticle();

        log.info("热点文章分值计算调度任务执行完成");

        return ReturnT.SUCCESS;
    }

}
