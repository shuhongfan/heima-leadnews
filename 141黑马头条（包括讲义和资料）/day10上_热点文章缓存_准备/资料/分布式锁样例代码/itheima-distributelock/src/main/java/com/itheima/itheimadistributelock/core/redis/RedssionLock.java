package com.itheima.itheimadistributelock.core.redis;

import org.redisson.Redisson;
import org.redisson.RedissonFairLock;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;


public class RedssionLock {
	//zookeeper 分布锁
	public static RLock getLock() {
		Config config = new Config();
		//指定使用单节点部署方式
		config.useSingleServer().setAddress("redis://127.0.0.1:6379");//.setPassword("mkxiaoer");
		config.useSingleServer().setConnectionPoolSize(500);//设置对于master节点的连接池中连接数最大为500
		config.useSingleServer().setIdleConnectionTimeout(10000);//如果当前连接池里的连接数量超过了最小空闲连接数，而同时有连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里去掉。时间单位是毫秒。
		config.useSingleServer().setConnectTimeout(30000);//同任何节点建立连接时的等待超时。时间单位是毫秒。
		config.useSingleServer().setTimeout(3000);//等待节点回复命令的时间。该时间从命令发送成功时开始计时。
		config.useSingleServer().setPingTimeout(30000);
		//获取RedissonClient对象
		RedissonClient redisson = Redisson.create(config);
		//获取锁对象  RLock
		RLock rLock = redisson.getLock("lock.lock");//封装但是了解和学习原理
		return rLock;
	}

	/**
	 * 演示可重入锁
	 * @param args
	 */
	public static void main(String[] args) {
		RLock lock = RedssionLock.getLock();
		lock.lock(30, TimeUnit.SECONDS);
		System.out.println("第一次加锁成功");
		// 一坨坨外部代码
		// 嵌套功能也需要加锁
		lock.lock(30, TimeUnit.SECONDS);
		System.out.println("第二次加锁成功");

		lock.unlock();
		System.out.println("第一次解锁成功");
		// 嵌套功能解锁
		// 整个方法解锁
		lock.unlock();
		System.out.println("第二次解锁成功");
	}
}
