package com.heima.wemedia;

import com.heima.wemedia.service.WmNewsAutoScanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author mrchen
 * @date 2022/4/28 11:46
 */
@SpringBootTest
public class WmAutoScanTest {

    @Autowired
    WmNewsAutoScanService wmNewsAutoScanService;

    @Test
    public void autoScan(){
        wmNewsAutoScanService.autoScanWmNews(6270);
    }
}
