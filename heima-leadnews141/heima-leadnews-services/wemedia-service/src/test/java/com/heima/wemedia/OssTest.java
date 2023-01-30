package com.heima.wemedia;

import com.heima.file.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author mrchen
 * @date 2022/4/24 15:12
 */
@SpringBootTest
public class OssTest {
    // 默认注入 oss实例
    @Autowired
    FileStorageService fileStorageService;

    @Value("${file.oss.prefix}")
    String prefix;

    @Value("${file.oss.web-site}")
    String webSite;

    @Autowired
    @Qualifier("minIOFileStorageService")
    FileStorageService fileStorageService2;

    @Value("${file.minio.readPath}")
    String readPath;

    @Test
    public void uploadMinio() throws FileNotFoundException {
        String path = fileStorageService2.store("test", "list.html", "text/html", new FileInputStream("C:\\work\\list.html"));

        System.out.println(readPath + path);
    }


    @Test
    public void upload() throws FileNotFoundException {
        // prefix: 文件存入oss 中bucket的哪个文件夹中
        // filename:  存入到oss中的文件名称
        // prefix + 2022 / 04 / 24 / meinv002.jpg
        FileInputStream fileInputStream = new FileInputStream("C:/worksoft/picture/mv004.jpg");

        // 文件在OSS中的实际路径:   prefix + / 2022 / 04 / 24 / meinv002.jpg
        String store = fileStorageService.store(prefix, "meinv003.jpg", fileInputStream);
        System.out.println("文件在OSS中的路径: " + store);
        System.out.println("文件的访问地址: " + (webSite + store));
    }
    @Test
    public void delete(){
        fileStorageService.delete("material/2022/4/20220424/meinv002.jpg");
    }


}
