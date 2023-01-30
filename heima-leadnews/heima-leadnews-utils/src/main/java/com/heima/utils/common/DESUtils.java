package com.heima.utils.common;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DESUtils {

    public static final String key = "12345678";

    /**
     * 加密
     * @param content
     * @param keyBytes
     * @return
     */
    private static byte[] encrypt(byte[] content, byte[] keyBytes) {
        try {
            DESKeySpec keySpec = new DESKeySpec(keyBytes);
            String algorithm =  "DES";//指定使什么样的算法
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
            SecretKey key = keyFactory.generateSecret(keySpec);
            String transformation = "DES/CBC/PKCS5Padding"; //用什么样的转型方式
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(keySpec.getKey()));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     * @param content
     * @param keyBytes
     * @return
     */
    private static byte[] decrypt(byte[] content, byte[] keyBytes) {
        try {
            DESKeySpec keySpec = new DESKeySpec(keyBytes);
            String algorithm = "DES";
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm );
            SecretKey key = keyFactory.generateSecret(keySpec);
            String transformation = "DES/CBC/PKCS5Padding";
            Cipher cipher = Cipher.getInstance(transformation );
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(keyBytes));
            byte[] result = cipher.doFinal(content);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 二进制转16进制
     * @param bytes
     * @return
     */
    private static String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        String sTemp;
        for (int i = 0; i<bytes.length; i++) {
            sTemp = Integer.toHexString(0xFF & bytes[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 16进制字符串转bytes
     * @param hex
     * @return
     */
    public static byte[] hexStringToByte(String hex) {
        int len = 0;
        int num=0;
        //判断字符串的长度是否是两位
        if(hex.length()>=2){
            //判断字符喜欢是否是偶数
            len=(hex.length() / 2);
            num = (hex.length() % 2);
            if (num == 1) {
                hex = "0" + hex;
                len=len+1;
            }
        }else{
            hex = "0" + hex;
            len=1;
        }
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }
    private static int toByte(char c) {
        if (c >= 'a')
            return (c - 'a' + 10) & 0x0f;
        if (c >= 'A')
            return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }

    private static byte[] hexToByteArr(String strIn) {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }
    /**
     * 加密
     * @param pass
     * @return
     */
    public static String encode(String pass){
        return byteToHexString(encrypt(pass.getBytes(), key.getBytes()));
    }

    /**
     * 解密
     * @param passcode
     * @return
     */
    public static String decode(String passcode){
        return byteToHexString(decrypt(hexToByteArr(passcode), key.getBytes()));
    }

    public static void main(String[] args) {
        String content = "password111111111111111";

        System.out.println("加密前 "+ byteToHexString(content.getBytes()));
        byte[] encrypted = encrypt(content.getBytes(), key.getBytes());
        System.out.println("加密后："+ byteToHexString(encrypted));

        byte[] decrypted=decrypt(encrypted, key.getBytes());
        System.out.println("解密后："+ byteToHexString(decrypted));


        System.out.println(encode(content));
        String s = new String(hexStringToByte(decode("159CF72C0BD2A8183D536215768C2E91556D77642F214E34")));
        System.out.println(s);
    }
}