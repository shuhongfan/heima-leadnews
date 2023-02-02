package com.heima.wemedia;

public class ThreadLocalDemo {
    public static void main(String[] args) throws InterruptedException {
        ThreadLocal<Integer> initLocal = new ThreadLocal<Integer>() {
            @Override
            protected Integer initialValue() {
                return 0;
            }
        };

        initLocal.set(1);

        new Thread(()->{
            initLocal.set(initLocal.get() + 1);
            System.out.println("值1："+initLocal.get());
        }).start();

        Thread.sleep(100);
        System.out.println("值2："+initLocal.get());
    }
}
