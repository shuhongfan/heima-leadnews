package com.heima.wemedia;

import com.heima.aliyun.scan.GreenScan;
import com.heima.aliyun.scan.ScanResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class SecurityTest {

    @Autowired
    private GreenScan greenScan;

    /**
     * 文本检测
     * @throws Exception
     */
    @Test
    public void testText() throws Exception{
        ScanResult scanResult = greenScan.greenTextScan("我是一个文本,冰毒买卖是违法的");
        System.out.println(scanResult);
    }

    /**
     * 图片检测
     * @throws Exception
     */
    @Test
    public void testImage() throws Exception {
        List<String> images = new ArrayList<>();
        images.add("https://heimaleadnewsoss.oss-cn-shanghai.aliyuncs.com/material/2021/1/20210112/205cd5d3346a48b59352c92808709da1.jpg");
        ScanResult scanResult = greenScan.imageUrlScan(images);
        System.out.println(scanResult);
    }

}
