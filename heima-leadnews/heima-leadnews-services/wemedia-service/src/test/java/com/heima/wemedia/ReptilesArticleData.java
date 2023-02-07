package com.heima.wemedia;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.file.service.FileStorageService;
import com.heima.model.threadlocal.WmThreadLocalUtils;
import com.heima.model.wemedia.dtos.NewsAuthDTO;
import com.heima.model.wemedia.dtos.WmNewsDTO;
import com.heima.model.wemedia.pojos.WmMaterial;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.service.WmNewsService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

/**
 * 抓取新闻数据  用于app端演示
 **/
@SpringBootTest
public class ReptilesArticleData {
    @Autowired
    WmNewsService wmNewsService;
    @Autowired
    WmMaterialMapper wmMaterialMapper;
    @Autowired
    FileStorageService fileStorageService;
    @Value("${file.oss.web-site}")
    String webSite;

    /**
     * 直接通过调用修改状态方法，将所有爬虫数据 设置通过
     */
    @Test
    public void authPassWmNews(){
        List<WmNews> list = wmNewsService.list(Wrappers.<WmNews>lambdaQuery().eq(WmNews::getStatus, 3));
        for (WmNews wmNews : list) {
            NewsAuthDTO dto = new NewsAuthDTO();
            dto.setId(wmNews.getId());
            wmNewsService.updateStatus((short)4,dto);
        }
    }

    /**
     * 直接通过调用修改状态方法，将所有爬虫数据 设置通过
     */
    Document document = null;
    @BeforeEach
    public void initDriver(){
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://3g.163.com/touch/news/sub/history/?ver=c&clickfrom=index2018_header_main");
        document = Jsoup.parse(driver.getPageSource());
    }

    /**
     * 获取数据  并封装成wmNews
     * @throws IOException
     */
    @Test
    public void reptilesData() throws IOException {
        // 模拟当前自媒体登录用户
        WmUser wmUser = new WmUser();
        wmUser.setId(1102);
        WmThreadLocalUtils.setUser(wmUser);
        // 获取网易新闻数据
        // 娱乐频道:https://3g.163.com/touch/ent/?ver=c&clickfrom=index2018_header_main
//        String url = "https://3g.163.com/touch/ent/?ver=c&clickfrom=index2018_header_main";
        // 获取该网页document文档数据
//        Document document = Jsoup.connect(url)
//                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36")
//                .get();
        // 找到指定class的所有元素
        Elements elementsByClass = document.getElementsByClass("tab-content");
        System.out.println("div元素个数:  " + elementsByClass.size());
        Element element = elementsByClass.get(0);
        // 找到所有article标签页面元素
        Elements articleList = element.getElementsByTag("article");
        for (Element article : articleList) {

            String href = null;
            String title = null;
            Elements imgList = null;
            try {
                Element aElement = article.getElementsByTag("a").get(0);
                href = aElement.attr("href");
                System.out.println("新闻的url路径: "+href);
                Element titleEle = aElement.getElementsByClass("title").get(0);
                title = titleEle.text();
                System.out.println("新闻的文章标题:"+title);
                Element newsPic = aElement.getElementsByClass("news-pic").get(0);
                // 获取封面图片元素集合
                imgList = newsPic.getElementsByTag("img");
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            // 封装WmNewsDto对象
            WmNewsDTO wmNewsDto = new WmNewsDTO();
            // 解析单个文章详情
            List<Map> contentMap = parseContent(href);
            wmNewsDto.setContent(JSON.toJSONString(contentMap));
            // 封面图片集合
            List<String> urlList = new ArrayList<>();
            for (Element imgEle : imgList) {
                String src = imgEle.attr("data-src");
                System.out.println("封面图片url==>"+src);
                String fileName = uploadPic(src);
                if(StringUtils.isNotBlank(fileName)){
                    // 如果上传图片路径不为空  创建素材信息
                    WmMaterial wmMaterial = new WmMaterial();
                    wmMaterial.setUserId(WmThreadLocalUtils.getUser().getId());
                    wmMaterial.setUrl(fileName);
                    wmMaterial.setType((short)0);
                    wmMaterial.setIsCollection((short)0);
                    wmMaterial.setCreatedTime(new Date());
                    wmMaterialMapper.insert(wmMaterial);
                    urlList.add(fileName);
                }
            }
            wmNewsDto.setTitle(title);
            wmNewsDto.setType((short) urlList.size());
            if(urlList.size()>0){
                wmNewsDto.setImages(urlList);
            }
            wmNewsDto.setChannelId(6);
            try {
                Thread.sleep(1000); // 睡眠1秒 让发布时间不一致
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wmNewsDto.setPublishTime(new Date());
            wmNewsDto.setStatus((short)3); // 待审核状态
            wmNewsDto.setLabels("爬虫");
            wmNewsService.submitNews(wmNewsDto);
        }
    }
    /**
     * 封面图片url==>//nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2020%2F0227%2Fd5ba8ca7j00q6cggh000vc000hs00b7m.jpg&thumbnail=234x146&quality=85&type=jpg
     * @param imgUrl
     * @return
     */
    public String uploadPic(String imgUrl){
        String imageUrl = getImageUrl(imgUrl);
        InputStream in = getInputStreamByUrl(imageUrl);
        String uuid = UUID.randomUUID().toString().replace("-","");
        String suffix = imageUrl.substring(imageUrl.lastIndexOf("."));
        if(in!=null){
            String fileName = fileStorageService.store("material", uuid+suffix, in);
            System.out.println("上传文件名称: "+fileName);
            return fileName;
        }
        return null;
    }
    public String getImageUrl(String url)  {
        try {
//            String url = "//nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2020%2F0227%2Fd5ba8ca7j00q6cggh000vc000hs00b7m.jpg&thumbnail=234x146&quality=85&type=jpg";
            url = url.split("&")[0];
            url = url.substring(url.indexOf("url=")).replaceAll("url=","");
            url = URLDecoder.decode(url, "utf8");
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解析图片路径出现异常");
        }
    }
    /**
     * 工具方法:  根据url路径 获取输入流
     * @param strUrl
     * @return
     */
    public static InputStream getInputStreamByUrl(String strUrl){
        if(!strUrl.startsWith("http")){
            strUrl = "http:" + strUrl;
        }
        HttpURLConnection conn = null;
        try {
            URL url = new URL(strUrl);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20 * 1000);
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy(conn.getInputStream(),output);
            return  new ByteArrayInputStream(output.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                if (conn != null) {
                    conn.disconnect();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 解析文章详情内容
     * @param href
     * @return
     */
    private List<Map> parseContent(String href)  {
        String url="http:"+href;
        List<Map> contentMap = new ArrayList<>();
        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36")
                    .get();
            Element contentDiv = document.getElementsByClass("content").get(0);
            Element pageDiv = contentDiv.getElementsByTag("div").get(0);
            Elements allElements = pageDiv.getAllElements();
            for (Element subElement : allElements) {
                String tagName = subElement.tagName();
                String className = subElement.className();
                if("p".equalsIgnoreCase(tagName)){
                    Map map = new HashMap();
                    map.put("type","text");
                    map.put("value",subElement.text());
                    contentMap.add(map);
                    System.out.println("文本内容: " + subElement.text());
                }
                if("div".equalsIgnoreCase(tagName)&&"photo".equalsIgnoreCase(className)){
                    System.out.println("图片内容: " + subElement.text());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map map = new HashMap();
            map.put("type","text");
            map.put("value","测试文章内容");
            contentMap.add(map);
        }
        return contentMap;
    }
}