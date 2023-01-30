package com.heima.utils.common;

/**
 * 分片桶字段算法
 */
public class BurstUtils {

    public final static String SPLIT_CHAR = "-";

    /**
     * 用-符号链接
     * @param fileds
     * @return
     */
    public static String encrypt(Object... fileds){
        StringBuffer sb  = new StringBuffer();
        if(fileds!=null&&fileds.length>0) {
            sb.append(fileds[0]);
            for (int i = 1; i < fileds.length; i++) {
                sb.append(SPLIT_CHAR).append(fileds[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 默认第一组
     * @param fileds
     * @return
     */
    public static String groudOne(Object... fileds){
        StringBuffer sb  = new StringBuffer();
        if(fileds!=null&&fileds.length>0) {
            sb.append("0");
            for (int i = 0; i < fileds.length; i++) {
                sb.append(SPLIT_CHAR).append(fileds[i]);
            }
        }
        return sb.toString();
    }
}
