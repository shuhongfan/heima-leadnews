package com.heima.freemarker.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Student {
    private String name;//姓名
    private int age;//年龄
    private Date birthday;//生日
    private Float money;//钱包
}