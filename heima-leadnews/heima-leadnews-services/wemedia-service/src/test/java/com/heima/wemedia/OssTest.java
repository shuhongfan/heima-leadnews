package com.heima.wemedia;

import com.heima.file.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
public class OssTest {

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 文件存入OSS中bucket的哪个文件夹中
     */
    @Value("${file.oss.prefix}")
    private String prefix;

    @Value("${file.oss.web-site}")
    private String webSite;

    @Test
    public void upload() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\shuho\\OneDrive\\图片\\1.jpg");
        String store = fileStorageService.store(prefix, "new.jpg", fileInputStream);
        System.out.println("文件在OSS中的路径：" + store);
        System.out.println("文件访问地址：" + webSite + store);
    }

    @Test
    public void delete() {
        fileStorageService.delete("material/2023/2/20230202/new.jpg");
    }
}
