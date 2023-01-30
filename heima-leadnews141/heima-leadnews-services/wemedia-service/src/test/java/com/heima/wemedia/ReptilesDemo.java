package com.heima.wemedia;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;

/**
 * @author mrchen
 * @date 2022/5/2 16:19
 */
public class ReptilesDemo {
    @Test
    public void test() throws IOException {

        System.setProperty("webdriver.chrome.driver", "C:\\worksoft\\tools\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://3g.163.com/touch/ent/?ver=c&clickfrom=index2018_header_main");
        Document document = Jsoup.parse(driver.getPageSource());

//        Document document = Jsoup.connect("https://3g.163.com/touch/ent/?ver=c&clickfrom=index2018_header_main")
//                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36")
//                .get();

        Elements articleEles = document.getElementsByTag("article");
        for ( Element articleEle: articleEles) {
            Element aEle = articleEle.getElementsByTag("a").get(0);
            String href = aEle.attr("href");
            System.out.println("文章详情:   " + href);

            Element titleEle = aEle.getElementsByClass("title").get(0);
            String text = titleEle.text();
            System.out.println("文章标题:   " + text);

            Elements imgEles = aEle.getElementsByTag("img");
            for (Element imgEle : imgEles) {
                System.out.println("封面图片data-src : " + imgEle.attr("data-src"));
                System.out.println("封面图片src : " + imgEle.attr("src"));
            }
        }
    }
}
