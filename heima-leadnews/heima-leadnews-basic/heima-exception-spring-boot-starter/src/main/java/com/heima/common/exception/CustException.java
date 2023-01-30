package com.heima.common.exception;

import com.heima.model.common.enums.AppHttpCodeEnum;
/**
 * @Description:  抛异常工具类
 * @Version: V1.0
 */
public class CustException {
    public static void cust(AppHttpCodeEnum codeEnum) {
        throw new CustomException(codeEnum );
    }
    public static void cust(AppHttpCodeEnum codeEnum,String msg) {
        throw new CustomException(codeEnum,msg);
    }
}