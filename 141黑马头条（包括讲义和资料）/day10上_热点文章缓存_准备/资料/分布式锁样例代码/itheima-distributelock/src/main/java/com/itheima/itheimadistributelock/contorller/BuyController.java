package com.itheima.itheimadistributelock.contorller;

import com.itheima.itheimadistributelock.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @作者 itcast
 * @创建日期 2020/7/1 10:44
 **/
@RestController
public class BuyController {
    @Autowired
    @Qualifier("goodsService")
    GoodsService goodsService;
    @RequestMapping("buy/{goodsId}/{num}")
    public boolean buy(@PathVariable String goodsId,@PathVariable int num){
        String userId = "001";
        boolean buy = goodsService.buy(userId, goodsId, num);
        return buy;
    }
}
