/**
 * TODO木子鱼系统平台<br/>
 * com.mzy.lock<br/>
 * SellTicket.java<br/>
 *  创建人:mofeng <br/>
 * 时间：2019年1月14日-下午4:39:43 <br/>
 * 2019木子鱼公司-版权所有<br/>
 */
package com.itheima.itheimadistributelock.service.impl;

import com.itheima.itheimadistributelock.config.SellConstants;
import com.itheima.itheimadistributelock.dao.GoodStockDao;
import com.itheima.itheimadistributelock.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;


@Service("goodsService")
@Scope("prototype")
public class GoodsServiceImpl  implements GoodsService {
	@Autowired
	private GoodStockDao goodStockDao;
	/**
	 * 未做任何处理的 购买商品方法
	 * @param userId 用户ID
	 * @param goodId 商品ID
	 * @param buyNum 购买数量
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean buy(String userId,String goodId,int buyNum){
		boolean result;
		// 根据商品id查询商品的库存数还有多少
		int num = goodStockDao.getStock(goodId);
		// 如果你的购买数量大于库存数量的。直接提示商品库存不足。
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
		return result;
	}
}
