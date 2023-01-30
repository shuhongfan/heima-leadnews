package com.heima.utils.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    /**
     * MD5加密
     * @param str
     * @return
     */
    public final static String encode(String str) {
        try {
            //创建具有指定算法名称的摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            //使用指定的字节数组更新摘要
            md.update(str.getBytes());
            //进行哈希计算并返回一个字节数组
            byte mdBytes[] = md.digest();
            String hash = "";
            //循环字节数组
            for (int i = 0; i < mdBytes.length; i++) {
                int temp;
                //如果有小于0的字节,则转换为正数
                if (mdBytes[i] < 0)
                    temp = 256 + mdBytes[i];
                else
                    temp = mdBytes[i];
                if (temp < 16)
                    hash += "0";
                //将字节转换为16进制后，转换为字符串
                hash += Integer.toString(temp, 16);
            }
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String encodeWithSalt(String numStr, String salt) {
        return encode(encode(numStr) + salt);
    }

    public static void main(String[] args) {
        System.out.println(encode("test"));//e10adc3949ba59abbe56e057f20f883e
        System.out.println(encodeWithSalt("123456","123456"));//5f1d7a84db00d2fce00b31a7fc73224f
    }
}