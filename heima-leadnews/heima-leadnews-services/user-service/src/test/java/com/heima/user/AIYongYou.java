package com.heima.user;


import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@SpringBootTest
public class AIYongYou {
    @Test
    public void idCardTest() {
        RestTemplate restTemplate = new RestTemplate();

//        设置请求头信息
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("apicode","38b26747fe1d4d2f9d80a66580186b7e");
        httpHeaders.setContentType(MediaType.APPLICATION_JSON); // json

//        请求参数
        Map<String,String> map = new HashMap<>();
        map.put("idNumber","210103195103222113");
        map.put("userName","王东镇");

//        封装请求参数
        HttpEntity<String> httpEntity = new HttpEntity<>(JSON.toJSONString(map), httpHeaders);

//        发送POST请求
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity("https://api.yonyoucloud.com/apis/dst/matchIdentity/matchIdentity", httpEntity, String.class);
        System.out.println(stringResponseEntity);
    }

}
