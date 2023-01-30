package com.heima.utils.common;


import com.heima.model.wemedia.pojos.WmNews;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

public class ProtostuffUtil {

    /**
     * 序列化
     * @param t
     * @param <T>
     * @return
     */
    public static <T> byte[] serialize(T t){
        Schema schema = RuntimeSchema.getSchema(t.getClass());
        return ProtostuffIOUtil.toByteArray(t,schema,
                LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
 
    }

    /**
     * 反序列化
     * @param bytes
     * @param c
     * @param <T>
     * @return
     */
    public static <T> T deserialize(byte []bytes,Class<T> c) {
        T t = null;
        try {
            t = c.newInstance();
            Schema schema = RuntimeSchema.getSchema(t.getClass());
             ProtostuffIOUtil.mergeFrom(bytes,t,schema);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * jdk序列化与protostuff序列化对比
     * @param args
     */
    public static void main(String[] args) {
        long start =System.currentTimeMillis();
        for (int i = 0; i <1000000 ; i++) {
            WmNews wmNews =new WmNews();
            JdkSerializeUtil.serialize(wmNews);
        }
        System.out.println(" jdk 花费 "+(System.currentTimeMillis()-start));

        start =System.currentTimeMillis();
        for (int i = 0; i <1000000 ; i++) {
            WmNews wmNews =new WmNews();
            ProtostuffUtil.serialize(wmNews);
        }
        System.out.println(" protostuff 花费 "+(System.currentTimeMillis()-start));
    }

 
 
}
