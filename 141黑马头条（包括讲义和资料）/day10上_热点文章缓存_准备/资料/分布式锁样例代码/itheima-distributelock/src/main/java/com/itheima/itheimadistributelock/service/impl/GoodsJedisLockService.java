/**
 * TODO木子鱼系统平台<br/>
 * com.mzy.lock<br/>
 * SellTicket.java<br/>
 *  创建人:mofeng <br/>
 * 时间：2019年1月14日-下午4:39:43 <br/>
 * 2019木子鱼公司-版权所有<br/>
 */
package com.itheima.itheimadistributelock.service.impl;

import com.itheima.itheimadistributelock.config.RedisPool;
import com.itheima.itheimadistributelock.config.SellConstants;
import com.itheima.itheimadistributelock.core.redis.DistrbutelockByRedis;
import com.itheima.itheimadistributelock.dao.GoodStockDao;
import com.itheima.itheimadistributelock.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.UUID;

@Service("goodsJedisLockService")
@Scope("prototype")
public class GoodsJedisLockService implements GoodsService {
	@Autowired
	private GoodStockDao goodStockDao;

	@Autowired
	DistrbutelockByRedis lock;

	@Transactional(rollbackFor = Exception.class)
	public  boolean buy(String userId,String goodId,int buyNum){
		Jedis jedis = RedisPool.getJedis();
		String requestId = UUID.randomUUID().toString();
		try {
			lock.lock(jedis,"lock",requestId,10);// 加锁
			boolean result = false;
			int num = goodStockDao.getStock(goodId);
			if(num < buyNum){
				System.out.println("商品库存不足，不在扣减....");
				return false;
			}
			System.out.println("用户"+userId+",扣减商品："+goodId+"，数量：" + buyNum);
			result =goodStockDao.buy(userId,goodId,buyNum);
			if(result){
				SellConstants.sellNum.addAndGet(buyNum);
			}
			System.out.println("用户"+userId+",扣减商品之后："+goodId+"，结果是：" + result);
			//任务执行完毕 关闭锁
			return result;
		}catch (Exception ex){
		}finally {
			if(lock!=null)lock.unlock(jedis,"lock",requestId);
			if(jedis!=null)jedis.close();
		}
		return false;
	}
}
