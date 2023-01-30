package com.heima.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.heima.gateway.util.AppJwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mrchen
 * @date 2022/4/22 11:11
 */
@Component
@Order(-1)
@Slf4j
public class AuthorizeFilter implements GlobalFilter {
    private static List<String> urlList = new ArrayList<>();
    // 初始化白名单 url路径
    static {
        urlList.add("/login/in");
        urlList.add("/v2/api-docs");
        urlList.add("/login_auth");
    }
    /**
     * 过滤方法
     * @param exchange
     * @param chain 过滤器链
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 1. 获取用户请求的uri地址
        String path = request.getURI().getPath();
        // 2. 判断该uri是否属于白名单路径  如果属于放行
        for (String allowUri : urlList) {
            if (path.contains(allowUri)) {
                //放行
                return chain.filter(exchange);
            }
        }
        // 3. 获取请求头中的token   如果为空返回 401 并终止请求
        String token = request.getHeaders().getFirst("token");
        if (StringUtils.isBlank(token)) {
//            // 返回401状态
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            // 终止
//            return response.setComplete();
            return writeMessage(exchange, "token不能为空");
        }
        // 4. 校验并解析token
        try {
            Claims claimsBody = AppJwtUtil.getClaimsBody(token);
            int i = AppJwtUtil.verifyToken(claimsBody);
            if(i < 1){
                // 5. 如果解析成功 获取token中存放的用户id , 并且将用户id设置到请求头中 路由给其它微服务
                Object id = claimsBody.get("id");
                // 将用户id设置到请求头中 路由给其它微服务
                request.mutate().header("userId",String.valueOf(id));
                // 认证成功放行请求
                return chain.filter(exchange);
            }else {
                log.error("解析token失败 原因: token已过期");
                return writeMessage(exchange,"解析token失败 原因: token已过期");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("解析token失败 原因: {}",e.getMessage());
            return writeMessage(exchange,"解析token失败 原因:" + e.getMessage());
        }
//        // 6. 如果解析失败 回 401 并终止请求
//        // 返回401状态
//        response.setStatusCode(HttpStatus.UNAUTHORIZED);
//        // 终止
//        return response.setComplete();
    }


    /**
     * 返回错误提示信息
     * @return
     */
    private Mono<Void> writeMessage(ServerWebExchange exchange, String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", HttpStatus.UNAUTHORIZED.value());
        map.put("errorMessage", message);
        //获取响应对象
        ServerHttpResponse response = exchange.getResponse();
        //设置状态码
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        //response.setStatusCode(HttpStatus.OK);
        //设置返回类型
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        //设置返回数据
        DataBuffer buffer = response.bufferFactory().wrap(JSON.toJSONBytes(map));
        //响应数据回浏览器
        return response.writeWith(Flux.just(buffer));
    }
}
