package com.heima.gateway.utils;

import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

public class AppJwtUtil {
    // TOKEN的有效期一天（S）
    private static final int TOKEN_TIME_OUT = 3_600;
    // 加密KEY
    private static final String TOKEN_ENCRY_KEY = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY";
    // 最小刷新间隔(S)
    private static final int REFRESH_TIME = 300;

    // 生产ID
    public static String getToken(Long id) {
        Map<String, Object> claimMaps = new HashMap<>();
        claimMaps.put("id", id);
        long currentTime = System.currentTimeMillis();
        return Jwts.builder().setId(UUID.randomUUID().toString()).setIssuedAt(new Date(currentTime))  //签发时间
                .setSubject("system")  //说明
                .setIssuer("heima") //签发者信息
                .setAudience("app")  //接收用户
                .compressWith(CompressionCodecs.GZIP)  //数据压缩方式
                .signWith(SignatureAlgorithm.HS512, generalKey()) //加密方式
                .setExpiration(new Date(currentTime + TOKEN_TIME_OUT * 1000))  //过期时间戳
                .addClaims(claimMaps) //cla信息
                .compact();
    }

    /**
     * 获取token中的claims信息
     *
     * @param token
     * @return
     */
    private static Jws<Claims> getJws(String token) {
        return Jwts.parser().setSigningKey(generalKey()).parseClaimsJws(token);
    }

    /**
     * 获取payload body信息
     *
     * @param token
     * @return
     */
    public static Claims getClaimsBody(String token) {
        try {
            return getJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return null;
        }
    }

    /**
     * 获取hearder body信息
     *
     * @param token
     * @return
     */
    public static JwsHeader getHeaderBody(String token) {
        return getJws(token).getHeader();
    }

    /**
     * 是否过期
     *
     * @param claims
     * @return -1：有效，0：有效，1：过期，2：过期
     */
    public static int verifyToken(Claims claims) {
        if (claims == null) {
            return 1;
        }
        try {
            claims.getExpiration().before(new Date());
            // 需要自动刷新TOKEN
            if ((claims.getExpiration().getTime() - System.currentTimeMillis()) > REFRESH_TIME * 1000) {
                return -1;
            } else {
                return 0;
            }
        } catch (ExpiredJwtException ex) {
            return 1;
        } catch (Exception e) {
            return 2;
        }
    }

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getEncoder().encode(TOKEN_ENCRY_KEY.getBytes());
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    public static void main(String[] args) {
        // day01 代码


        String token = AppJwtUtil.getToken(1L);
        System.out.println(token);
        try {
            Claims claimsBody = getClaimsBody("eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAADWLQQqEMAwA_5KzhbQ0tvU3kUa2glBIBUX27xsPe5thmAf20WAB9MKSSnJr5uwiVnRMJTgsknIi2raIMEHjAYufA4USKc4T6LnarbcOOd6uavqRdrAZn9WMezeWq__P7N-zWfPfH6YheqeAAAAA.btWGNZaYIHoGm_RDZAE2Gg9kJWf_qXo0o7XWzozySwcXnX2n-dHKmqmrSrTnl-iPwU6BcydOQDMXWRxfVZpcUQ");
            int i = verifyToken(claimsBody);
            if(i<1){

                Object id = claimsBody.get("id");
                System.out.println("解析token成功 ==> 用户的id值 == "+ id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("解析token失败");
        }
    }

}
