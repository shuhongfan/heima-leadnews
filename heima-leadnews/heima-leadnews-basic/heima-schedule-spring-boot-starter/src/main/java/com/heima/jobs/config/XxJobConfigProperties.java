package com.heima.jobs.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "xxljob")
public class XxJobConfigProperties {
    // 调度中心地址
    private String adminAddress = "http://localhost:8888/xxl-job-admin";
    // 注册 执行器名称
    private String executorName = "default";
    // 当前执行器端口
    private int executorPort;
    // 日志文件输出路径
    private String logPath;
    // 注册到调度中心的  执行器地址
    private String executorAddress;
}