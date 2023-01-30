package com.heima.user;
import java.util.Date;

import com.heima.feigns.ArticleFeign;
import com.heima.feigns.WemediaFeign;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.pojos.WmUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author mrchen
 * @date 2022/4/22 15:13
 */
@SpringBootTest
public class FeignTest {

    @Autowired
    WemediaFeign wemediaFeign;

    @Test
    public void findWmUser(){
        ResponseResult<WmUser> responseResult = wemediaFeign.findByName("admin");
        WmUser wmUser = responseResult.getData();
        System.out.println(wmUser);

    }

    @Test
    public void saveWmUser(){

        WmUser wmUser = new WmUser();
        wmUser.setApUserId(1);
        wmUser.setName("sd");
        wmUser.setPassword("sadas");
        wmUser.setSalt("sad");
        wmUser.setNickname("12");
        wmUser.setImage("123");
        wmUser.setLocation("123");
        wmUser.setPhone("123");
        wmUser.setStatus(0);
        wmUser.setEmail("");
        wmUser.setType(0);
        wmUser.setScore(0);
        wmUser.setLoginTime(new Date());
        wmUser.setCreatedTime(new Date());

        ResponseResult<WmUser> responseResult = wemediaFeign.save(wmUser);
        System.out.println(responseResult);

    }



    @Autowired
    ArticleFeign articleFeign;

    @Test
    public void findApAuthorByUser(){
        ResponseResult<ApAuthor> responseResult = articleFeign.findByUserId(4);
        ApAuthor apAuthor = responseResult.getData();
        System.out.println(apAuthor);

        ApAuthor apAuthor1 = new ApAuthor();
        apAuthor1.setName("newname");
        apAuthor1.setType(2);
        apAuthor1.setUserId(10);
        apAuthor1.setCreatedTime(new Date());
        apAuthor1.setWmUserId(11);
        articleFeign.save(apAuthor1);
    }
}
