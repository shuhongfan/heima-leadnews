package com.itheima.itheimadistributelock.config;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.io.UnsupportedEncodingException;

public class MyZkSerializer implements ZkSerializer {

    private String charset = "UTF-8";

    @Override
    public byte[] serialize(Object data) throws ZkMarshallingError {
        try{
            return String.valueOf(data).getBytes(charset);
        }catch(UnsupportedEncodingException ex){
            throw  new ZkMarshallingError(ex);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
       try{
           return new String(bytes,charset);
       }catch(UnsupportedEncodingException ex){
            throw  new ZkMarshallingError(ex);
       }
    }
}
