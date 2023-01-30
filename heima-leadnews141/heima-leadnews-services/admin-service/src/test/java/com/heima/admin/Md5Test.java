package com.heima.admin;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author mrchen
 * @date 2022/4/22 9:27
 */
public class Md5Test {


    @Test
    public void md5(){
        //salt: pJzh8NPwTN
        //56a40b3740a8e0db7d56552a99e35f7e
        // 56a40b3740a8e0db7d56552a99e35f7e
        String salt = RandomStringUtils.randomAlphanumeric(10);//获取一个10位的随机字符串
        System.out.println("salt: " +salt);
        String password = DigestUtils.md5DigestAsHex(("hellopJzh8NPwTN").getBytes());
        System.out.println(password);
        // hellopJzh8NPwTN
    }
}
