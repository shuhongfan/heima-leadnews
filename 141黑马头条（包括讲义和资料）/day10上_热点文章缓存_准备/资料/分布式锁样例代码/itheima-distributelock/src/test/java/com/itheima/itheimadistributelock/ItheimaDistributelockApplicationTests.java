package com.itheima.itheimadistributelock;

import com.itheima.itheimadistributelock.config.SellConstants;
import com.itheima.itheimadistributelock.service.GoodsService;
import com.itheima.itheimadistributelock.service.impl.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItheimaDistributelockApplicationTests implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    long timed = 0;
    @Before
    public void start(){
        timed = System.currentTimeMillis();
        System.out.println("开始测试....");
    }
    @After
    public void end(){
        System.out.println("结束测试，执行时长是：" + (System.currentTimeMillis() - timed) / 1000 );
        System.out.println("共售出:"+ SellConstants.sellNum.get() + "台产品");
    }
    @Test
    public void buy(){
        //模拟请求数量
        int serviceNum =4;//4台tomcat
        int requesetSize = 100;//每台服务多少并发进入到系统
        //倒计数器。用于模拟高并发 juc CountDownLatch 主线分布式锁，线程的阻塞和唤醒jdk5 juc编程提供并发编程类
        CountDownLatch countDownLatch = new CountDownLatch(1);
        //循环创建N个线程
        List<Thread> threads = new ArrayList<>();
        String userId = "100",goodsId = "apple";
        int stock = 2;
        //模拟服务器的数量
        for (int i = 0; i < serviceNum; i++) {
            // 未使用锁
//            GoodsService goodsService = applicationContext.getBean(GoodsServiceImpl.class);
            // 使用 synchronized 锁
//            GoodsService goodsService = applicationContext.getBean(GoodsSyncSerivice.class);
            // 使用 zookeeper 实现分布式锁
            GoodsService goodsService = applicationContext.getBean(GoodsZkLockSerivice.class);
            // 使用 Jedis ==> Redis 实现分布式锁
//            GoodsService goodsService = applicationContext.getBean(GoodsJedisLockService.class);
            // 使用 Redission ==> Redis 实现分布式锁
//            GoodsService goodsService = applicationContext.getBean(GoodsRedissionLockSerivice.class);
            //模拟每台服务器发起请求的数量
            for (int i1 = 0; i1 < requesetSize; i1++) {
                Thread thread = new Thread(()->{
                    try {
                        //等待countdownlatch值为0，也就是其他线程就绪后，在运行后续的代码。
                        countDownLatch.await();
                        //执行吃饭的动作
                        goodsService.buy(userId,goodsId,stock);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                });
                //添加线程到集合中
                threads.add(thread);
                //启动线程
                thread.start();
            }
        }
        //并发执行所有请求
        countDownLatch.countDown();
        threads.forEach((e)->{
            try {
                e.join();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
