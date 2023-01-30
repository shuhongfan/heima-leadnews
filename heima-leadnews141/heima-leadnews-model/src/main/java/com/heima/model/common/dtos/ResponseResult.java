package com.heima.model.common.dtos;

import com.alibaba.fastjson.JSON;
import com.heima.model.common.enums.AppHttpCodeEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用的结果返回类
 * @param <T>
 */
@Setter
@Getter
public class ResponseResult<T> implements Serializable {
    private String host; // IP
    private Integer code = 200;  // 状态码
    private String errorMessage;  // 提示信息
    private T data;  // 数据 Object

    public ResponseResult() {
    }
    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }
    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.errorMessage = msg;
        this.data = data;
    }
    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.errorMessage = msg;
    }
    public static ResponseResult errorResult(int code, String msg) {
        ResponseResult result = new ResponseResult();
        return result.error(code, msg);
    }

    public static ResponseResult okResult(int code, String msg) {
        ResponseResult result = new ResponseResult();
        return result.ok(code, null, msg);
    }
    public static ResponseResult okResult(Object data) {
        ResponseResult result = setAppHttpCodeEnum(AppHttpCodeEnum.SUCCESS, AppHttpCodeEnum.SUCCESS.getErrorMessage());
        if(data!=null) {
            result.setData(data);
        }
        return result;
    }
    public static ResponseResult okResult() {
        return okResult(null);
    }
    public static ResponseResult errorResult(AppHttpCodeEnum enums){
        return setAppHttpCodeEnum(enums,enums.getErrorMessage());
    }
    public static ResponseResult errorResult(AppHttpCodeEnum enums, String errorMessage){
        return setAppHttpCodeEnum(enums,errorMessage);
    }
    public static ResponseResult setAppHttpCodeEnum(AppHttpCodeEnum enums){
        return okResult(enums.getCode(),enums.getErrorMessage());
    }
    private static ResponseResult setAppHttpCodeEnum(AppHttpCodeEnum enums, String errorMessage){
        return okResult(enums.getCode(),errorMessage);
    }
    public ResponseResult<?> error(Integer code, String msg) {
        this.code = code;
        this.errorMessage = msg;
        return this;
    }
    public ResponseResult<?> ok(Integer code, T data) {
        this.code = code;
        this.data = data;
        return this;
    }
    public ResponseResult<?> ok(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.errorMessage = msg;
        return this;
    }
    public ResponseResult<?> ok(T data) {
        this.data = data;
        return this;
    }

    public boolean checkCode() {
        if(this.getCode().intValue() != 0){
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
//        System.out.println("成功的响应:");
//        Map map = new HashMap();
//        map.put("phone","123123123");
//        map.put("name","xiaoming");
//        System.out.println( JSON.toJSONString(ResponseResult.okResult(map)));

//        System.out.println(JSON.toJSONString(ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR,"您的密码输入错误")));
    }
}
