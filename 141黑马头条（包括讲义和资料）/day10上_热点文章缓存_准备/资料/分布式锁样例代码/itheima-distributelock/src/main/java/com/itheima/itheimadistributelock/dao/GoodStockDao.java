package com.itheima.itheimadistributelock.dao;

import com.itheima.itheimadistributelock.config.BuyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GoodStockDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    // 查询商品的查询对应商品的库存
    public int getStock(String goodId){
        try{
            // transaction                                     select for update 阻塞
            String sql = "select goods_num from tb_goods where goods_code = ? for update";
//            String sql = "select goods_num from tb_goods where goods_code = ?";
            Integer integer = jdbcTemplate.queryForObject(sql,Integer.class,goodId);
            return  integer;
        }catch(Exception ex){
            ex.printStackTrace();
           return 0;
        }
    }
    // 扣减商品的库存数
    public boolean buy(String userId,String goodId,int stock){
        //商品数量减去1
        String sql = "update tb_goods set goods_num = goods_num - "+stock+" where goods_code = ?";
        //为什么不要使用innobdb的乐观锁，原因很简单：分布式项目中，并发太大可以造成mysql写入的压力太大。
//        String sql = "update tb_goods set goods_num = goods_num - "+stock+" where goods_code = ? and  goods_num -"+stock+">=0";
        int count = jdbcTemplate.update(sql,goodId);
        if(count!=1){
            throw  new BuyException("商品扣减失败...");
        }
        return true;
    }
}
