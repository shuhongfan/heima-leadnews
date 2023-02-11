package com.heima.comment.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class RedissionConfig {
    @Value("${spring.redis.host:localhost}")
    private String host; // redis地址
    @Value("${spring.redis.port:6379}")
    private String port; // redis端口
    @Value("${spring.redis.password}")
    private String password; // redis端口


    @Bean
    public RedissonClient likesLock() {
        Config config = new Config();
        //指定使用单节点部署方式
        config.useSingleServer()
                .setAddress("redis://"+host+":"+port+"");
        config.useSingleServer().setConnectionPoolSize(500);//设置对于master节点的连接池中连接数最大为500
        config.useSingleServer().setIdleConnectionTimeout(10000);//如果当前连接池里的连接数量超过了最小空闲连接数，而同时有连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里去掉。时间单位是毫秒。
        config.useSingleServer().setConnectTimeout(30000);//同任何节点建立连接时的等待超时。时间单位是毫秒。
        config.useSingleServer().setTimeout(3000);//等待节点回复命令的时间。该时间从命令发送成功时开始计时。
        config.useSingleServer().setPingTimeout(30000);
        config.useSingleServer().setPassword(password);
        //获取RedissonClient对象
        RedissonClient redisson = Redisson.create(config);
        //获取锁对象
        return redisson;
    }
}