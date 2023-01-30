package com.heim.oss;

/**
 * @author mrchen
 * @date 2022/4/24 16:06
 */
public class ThreadLocalDemo {
    public static void main(String[] args) throws InterruptedException {
        ThreadLocal<Integer> intLocal = new ThreadLocal<Integer>(){
            @Override
            protected Integer initialValue() {
                // 初始化的值
                return 0;
            }
        };
        // 变量设置为1
        intLocal.set(1);
        new Thread(
                ()->{
                    intLocal.set(intLocal.get() + 1);
                    System.out.println("值1: " + intLocal.get());
                }
        ).start();
        Thread.sleep(100);
        System.out.println("值2: " + intLocal.get());
    }
}
