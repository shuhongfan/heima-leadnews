package com.itheima.xxljob.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class HelloJob {

    @Value("${server.port}")
    private String appPort;

    @XxlJob("helloJob")
    public ReturnT<String> hello(String param) throws Exception {
        System.out.println("helloJob："+ LocalDateTime.now()+",端口号"+appPort);
        return ReturnT.SUCCESS;
    }
}