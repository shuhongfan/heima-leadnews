package com.heima.user;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mrchen
 * @date 2022/4/24 11:32
 */
public class AIYongyou {

    @Test
    public void idCardTest(){
        RestTemplate restTemplate = new RestTemplate();
        // 请求头的信息
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("apicode","baf99b5d34774adf8bb3f185bfc9785e");

        // 请求参数
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("idNumber","210103195103222113");
        paramMap.put("userName","王东镇");

        HttpEntity<String> httpEntity = new HttpEntity<>(JSON.toJSONString(paramMap),httpHeaders);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://api.yonyoucloud.com/apis/dst/matchIdentity/matchIdentity", httpEntity, String.class);
        System.out.println(responseEntity.getBody());
    }
}
