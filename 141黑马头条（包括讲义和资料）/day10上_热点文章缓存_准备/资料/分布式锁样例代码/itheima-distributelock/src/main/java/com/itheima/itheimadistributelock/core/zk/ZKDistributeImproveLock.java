package com.itheima.itheimadistributelock.core.zk;

import com.itheima.itheimadistributelock.config.MyZkSerializer;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/*
 * 基于有序节点 实现分布式锁
 **/
public class ZKDistributeImproveLock implements Lock {
    //znode 父节点
    private String lockPath;
    //zkclient客户端
    private ZkClient zkClient;
    //获取当前自己创建的节点，就相当于我在银行取票：当前我的序号
    private ThreadLocal<String> currentPath = new ThreadLocal<>();
    // 我关注的前一个人的序号。
    private ThreadLocal<String> beforePath = new ThreadLocal<>();
    // 初始化客户端和目录
    public ZKDistributeImproveLock(String lockPath){
        if(lockPath==null || lockPath.trim().equals("")){
            throw  new IllegalArgumentException("参数lockpath不能为空......");
        }
        this.lockPath = lockPath;
//        zkClient = new ZkClient("192.168.12.129:2181");
        zkClient = new ZkClient("127.0.0.1:2181");
        zkClient.setZkSerializer(new MyZkSerializer());
        //初始化父节点
        if(!this.zkClient.exists(lockPath)){
            try{
                this.zkClient.createPersistent(lockPath);
            }catch(ZkNodeExistsException ex){
               ex.printStackTrace();
            }
        }
    }
    @Override
    public void lock() {
        if(!tryLock()){
            //如果已经获取到锁了，那么久挂起.等待
            waitForLock();//这里一定要找个可以阻塞线程的机制，那么就是：CountDownLatch或者CyclicBarrier
            //再次尝试获取lock
            lock();
        }
    }
    // 等待挂起锁，找个能阻塞线程执行的。
    private void waitForLock(){
        // 定义线程阻塞
        CountDownLatch countDownLatch = new CountDownLatch(1);
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
        zkClient.subscribeDataChanges(this.beforePath.get(),listener);
        // 否则阻塞自己
        if(this.zkClient.exists(this.beforePath.get())){
            try{
                //被挂起
                countDownLatch.await();
            }catch(Exception ex){
               ex.printStackTrace();
            }
        }
        // 取消注册
        zkClient.unsubscribeDataChanges(this.beforePath.get(),listener);
    }

    @Override
    public boolean tryLock() {//不会阻塞
        // 1：首先判断当前有没有元素，如果没有就创建一个
        if(this.currentPath.get() == null || !this.zkClient.exists(this.currentPath.get())){
            String node = this.zkClient.createEphemeralSequential(lockPath+"/","locked");  // /lock/locked000000000
            //代表我以后取到号了。
            currentPath.set(node);
        }
        // 2：获取所有的子目录
        List<String> childrens = this.zkClient.getChildren(lockPath);
        // 3: 排序childrens
        Collections.sort(childrens);
        // 4:判断当前节点是否是最小
        if(currentPath.get().equals(lockPath+"/"+childrens.get(0))){
            return true;
        }else{
            //取前面一个 取得字节的索引号
            int currentIndex = childrens.indexOf(currentPath.get().substring(lockPath.length() + 1));
            String  node = lockPath + "/" + childrens.get(currentIndex - 1);
            beforePath.set(node);
        }
        return false;
    }
    @Override
    public void unlock() {
        if(this.currentPath.get()!=null) {
            zkClient.delete(this.currentPath.get());
            this.currentPath.set(null);
        }
    }



    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

}