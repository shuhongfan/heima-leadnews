package com.heima.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.heima.gateway.utils.AppJwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
 * 认证过滤器
 */
@Component
@Slf4j
@Order(-1) // 值越小越优先执行
public class AuthorizeFilter implements GlobalFilter {
    /**
     * 白名单集合
     */
    private static List<String> urlList = new ArrayList<>();
    // 初始化白名单 url路径
    static {
        urlList.add("/login/in");
        urlList.add("/v2/api-docs");
    }

    /**
     * 过滤方法
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        0. 获取请求和响应对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

//        1.获取用户请求的URL地址
        String path = request.getURI().getPath();

//        2. 判断url是否属于白名单路径，如果属于放行
        for (String url : urlList) {
            if (path.contains(url)) {
//                放行
                return chain.filter(exchange);
            }
        }

//        3. 获取请求头中的token 如果为空返回401，并终止请求
        String token = request.getHeaders().getFirst("token");
        if (StringUtils.isBlank(token)) {
//            返回401状态
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            终止
//            return response.setComplete();
            return writeMessage(exchange, "token不能为空");
        }

//        4. 校验并解析token
        try {
            Claims claimsBody = AppJwtUtil.getClaimsBody(token);
            int i = AppJwtUtil.verifyToken(claimsBody);
//            -1：有效，0：有效，1：过期，2：过期
            if (i < 1) {
//              5. 如果解析成功，获取token中存放的用户id，并且将用户id设置到请求头中，路由给其他微服务
                Object id = claimsBody.get("id");
//                将用户id设置到请求头中,路由给其他微服务
                request.mutate().header("userId", String.valueOf(id));
//                认证成功放行请求
                return chain.filter(exchange);
            } else {
                log.error("解析token失败,原因:{}", "token已过期");
                return writeMessage(exchange, "解析token失败,原因:token已过期");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("解析token失败,原因:{}",e.getMessage());
            return writeMessage(exchange, "解析token失败,原因:" + e.getMessage());
        }


//        6. 如果解析失败，返回401并终止请求
//        response.setStatusCode(HttpStatus.UNAUTHORIZED);
//        return response.setComplete();
    }

    /**
     * 返回错误提示信息
     * @param exchange
     * @param message
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
