package com.heima.tast.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * SpringTask
 * @author mrchen
 * @date 2022/5/5 17:06
 */
@Component
public class EatJob {
//    @Scheduled(cron = "*/3 * * * * ?")
    @XxlJob("helloJob")
    public ReturnT eat(String params){
        System.out.println(params+" 我开始干饭了  。。。 我要变成一个胖子   emo");
        return ReturnT.SUCCESS;
    }
}
