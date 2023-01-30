package com.itheima.itheimadistributelock.core.redis;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * 分布式锁  redis实现
 * 未实现: 可重入 公平锁  锁的自动续约 尝试锁  集群
 **/
@Component
public class DistrbutelockByRedis {
    public static final String UNLOCK_LUA;
    static {
        // 1        2
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }

    /**
     * // 如果存成功了  代表  得到了 锁
      // 如果  没存成功   接着尝试
     * @param jedis
     * @param lockKey
     * @param requestId
     * @param expireSecond
     */
    public void lock(Jedis jedis, String lockKey, String requestId, int expireSecond){
        long startTime = System.currentTimeMillis();
        while (true){
            // 获取锁  true
            boolean b = setLock(jedis, lockKey, requestId, expireSecond);
            // break
            if(b){
                break;
            }
            long endTime = System.currentTimeMillis();
            // 超时处理   如果  在超时时间内  还是没获取到锁  抛出 异常
            if((endTime-startTime)>30000){
                throw new RuntimeException("加锁超时");
            }
        }
    }
    // sexnx key
    public boolean setLock(Jedis jedis, String lockKey, String requestId, int expireSecond){
        // set 锁的名称 客户端ID nx ex
        // set lock 1 nx ex 100
        String set = jedis.set(lockKey, requestId, "nx", "ex", expireSecond);
        // 尝试往redis当中  存一条数据   order_lock_key
        if("OK".equalsIgnoreCase(set)){
            return true;
        }
        return false;
    }
    /**
     * 释放锁
     */
    public void unlock(Jedis jedis, String lockKey, String requestId){
        long startTime = System.currentTimeMillis();
        while (true){
            // 获取锁  true
            boolean b = releaseLock(jedis,lockKey,requestId);
            // break
            if(b){
                break;
            }
            long endTime = System.currentTimeMillis();
            // 超时处理   如果  在超时时间内  还是没获取到锁  抛出 异常
            if((endTime-startTime)>30000){
                throw new RuntimeException("释放锁超时");
            }
        }
    }
    /**
     * 释放锁 使用了lua脚本
     * ("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
     *         sb.append("then ");
     *         sb.append("    return redis.call(\"del\",KEYS[1]) ");
     *         sb.append("else ");
     *         sb.append("    return 0 ");
     *         sb.append("end ");
     *
     * @param jedis        2 =                                          [   get locked  del locked  ]
     * @param lockKey      2 == 2
     *                     del locked
     * @param requestId
     * @return
     */
    public boolean releaseLock(Jedis jedis, String lockKey, String requestId){
        List<String> keys = new ArrayList<>();
        keys.add(lockKey);
        List<String> vals = new ArrayList<>();
        vals.add(requestId);
        Long eval = (Long)jedis.eval(UNLOCK_LUA, keys, vals);
        if(eval!=null && eval>0){
            return true;
        }
        return false;
    }
}
