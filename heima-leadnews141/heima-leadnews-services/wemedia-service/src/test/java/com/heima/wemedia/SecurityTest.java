package com.heima.wemedia;

import com.heima.aliyun.scan.GreenScan;
import com.heima.aliyun.scan.ScanResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

/**
 * @author mrchen
 * @date 2022/4/28 9:34
 */
@SpringBootTest
public class SecurityTest {

    @Autowired
    GreenScan greenScan;


    @Test
    public void scanText()  {
        try {
            ScanResult scanResult = greenScan.greenTextScan("不要贩卖 冰毒 ，因为这是违法的");
            System.out.println(scanResult);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("阿里云调用失败，转为人工审核");
        }
    }

    @Test
    public void imageScan(){

        try {
            ScanResult scanResult = greenScan.imageUrlScan(Arrays.asList("https://leadnews141.oss-cn-shanghai.aliyuncs.com/material/2022/4/20220424/meinv003.jpg"));
            System.out.println(scanResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
