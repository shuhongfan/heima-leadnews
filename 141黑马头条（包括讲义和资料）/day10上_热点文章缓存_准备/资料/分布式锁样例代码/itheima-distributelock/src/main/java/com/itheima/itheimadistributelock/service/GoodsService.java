package com.itheima.itheimadistributelock.service;

public interface GoodsService {

    /**
     * 购买商品的方法
     * @param userId
     * @param goodId
     * @param buyNum
     * @return
     */
    boolean buy(String userId,String goodId,int buyNum);
}
