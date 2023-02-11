package com.heima.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class HelloJob {
    @Scheduled(cron = "0/5 * * * * ?")
    public void eat(){
        System.out.println("5秒中吃一次饭，我想成为一个胖子"+new Date());
    }
}