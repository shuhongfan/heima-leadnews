package com.itheima.itheimadistributelock.config;

public class BuyException extends RuntimeException {


    private String message;

    public BuyException(String s) {
        this.message = s;
    }


}
