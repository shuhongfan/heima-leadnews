package com.itheima.itheimadistributelock.core.zk;

import com.itheima.itheimadistributelock.config.MyZkSerializer;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/*
 * 基于zookeeper的临时节点机制 实现分布式锁
 *
 **/
public class ZKDistributeLock implements Lock {

    // znode父节点，用来收集和归纳是那种业务的锁
    private String lockPath ; // zookeeperLock
    //zkclient客户端
    private ZkClient zkClient;

    // 初始化客户端和目录
    public ZKDistributeLock(String lockPath){
        if(lockPath==null || lockPath.trim().equals("")){
            throw  new IllegalArgumentException("参数lockpath不能为空......");
        }
        this.lockPath = lockPath;
        // 连接zookeeper ,
//        zkClient = new ZkClient("127.0.0.1:2181");
        zkClient = new ZkClient("127.0.0.1:2181");
        // 设置序列化
        zkClient.setZkSerializer(new MyZkSerializer());
    }
    public static void main(String[] args) {
        ZKDistributeLock zkDistributeLock = new ZKDistributeLock("/mylock");
        boolean b = zkDistributeLock.tryLock();
        if (b){
            System.out.println("获取到锁了 执行业务代码");
            zkDistributeLock.unlock();
        }else {
            System.out.println("没获取到锁 等待其他客户端释放锁后再尝试获取");
        }

        zkDistributeLock.unlock();
    }


    @Override
    public void lock() {
        if(!tryLock()){
            //如果获取锁失败，那么就挂起.等待
            waitForLock();//这里一定要找个可以阻塞线程的机制，那么久是：CountDownLatch或者CyclicBarrier
            //再次尝试获取lock
            lock();
        }
    }

    // 等待挂起锁，找个能阻塞线程执行的。
    private void waitForLock(){
        // 定义线程阻塞，并发屏障   CountDownLatch
        CountDownLatch countDownLatch = new CountDownLatch(1); // 1
        // 监听节点变化
        IZkDataListener listener = new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
            }
            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                //如果节点被删除，立马释放。什么叫节点被删除呢？就是去调用unlock的时候执行 `zkClient.delete(lockPath);` 的时候回触发
                //说明释放锁了，那么放行让别的线程去执行获取锁操作。
                System.out.println("********************收到节点删除***************************");
                countDownLatch.countDown();
            }
        };
        // 监听节点变化
        // 如果lockPath子目录下 节点发生变化 监听方法会被触发
        zkClient.subscribeDataChanges(lockPath,listener);
        // 否则阻塞自己
        if(this.zkClient.exists(lockPath)){
            try{
                //被挂起
                countDownLatch.await(); // 线程阻塞
            }catch(Exception ex){
               ex.printStackTrace();
            }
        }
        // 取消注册
        zkClient.unsubscribeDataChanges(lockPath,listener);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    /**
     * 尝试创建临时节点:
     *  1. 创建成功返回true
     *  2. 创建失败返回false
     * @return
     */
    @Override
    public boolean tryLock() {//不会阻塞
        try{
            //创建临时节点，如果节点已经存在就会出现异常ZkNoNodeException，利用这个zk的这个特性
            //刚好满足排他性。
            zkClient.createEphemeral(lockPath);
        }catch(ZkNodeExistsException ex){
            return false;
        }
        return true;
    }


    @Override
    public void unlock() {
        // 删除节点，释放锁
        System.out.println("释放锁==>"+lockPath);
        zkClient.delete(lockPath);
    }



    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}