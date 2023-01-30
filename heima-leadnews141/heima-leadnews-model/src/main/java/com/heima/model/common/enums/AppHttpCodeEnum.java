package com.heima.model.common.enums;

public enum AppHttpCodeEnum {

    // 成功段0
    SUCCESS(0,"操作成功"),

    // 登录段1~50
    NEED_LOGIN(1,"需要登录后操作"),
    LOGIN_PASSWORD_ERROR(2,"密码错误"),
    LOGIN_STATUS_ERROR(3,"用户状态异常,请联系管理员"),

    // TOKEN50~100
    TOKEN_INVALID(50,"无效的TOKEN"),
    TOKEN_EXPIRE(51,"TOKEN已过期"),
    TOKEN_REQUIRE(52,"TOKEN是必须的"),
    // SIGN验签 100~120
    SIGN_INVALID(100,"无效的SIGN"),
    SIG_TIMEOUT(101,"SIGN已过期"),
    // 参数错误 500~1000
    PARAM_REQUIRE(500,"缺少参数"),
    PARAM_INVALID(501,"无效参数"),
    PARAM_IMAGE_FORMAT_ERROR(502,"图片格式有误"),
    SERVER_ERROR(503,"服务器内部错误"),
    REMOTE_SERVER_ERROR(504,"远程调用出错"),

    // 数据错误 1000~2000
    DATA_EXIST(1000,"数据已经存在"),
    AP_USER_DATA_NOT_EXIST(1001,"ApUser数据不存在"),
    DATA_NOT_EXIST(1002,"数据不存在"),
    DATA_NOT_ALLOW(1003,"数据不允许此操作"),
    // 数据错误 3000~3500
    NO_OPERATOR_AUTH(3000,"无权限操作"),
    NEED_ADMIND(3001,"需要管理员权限"),


    //###################admin code 4000-4999#######################
    ADMING_CHANNEL_NOT_EXIST(4000,"频道不存在"),
    ADMING_CHANNEL_STATUS_TRUE(4001,"频道有效，无法删除");

    int code;
    String errorMessage;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
