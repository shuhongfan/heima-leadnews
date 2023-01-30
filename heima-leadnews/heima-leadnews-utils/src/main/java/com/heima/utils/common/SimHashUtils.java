package com.heima.utils.common;

import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class SimHashUtils {
    /**
     * 清除html标签
     * @param content
     * @return
     */
    private static String cleanResume(String content) {
        // 若输入为HTML,下面会过滤掉所有的HTML的tag
        content = Jsoup.clean(content, Whitelist.none());
        content = StringUtils.lowerCase(content);
        String[] strings = {" ", "\n", "\r", "\t", "\\r", "\\n", "\\t", "&nbsp;"};
        for (String s : strings) {
            content = content.replaceAll(s, "");
        }
        return content;
    }


    /**
     * 这个是对整个字符串进行hash计算
     * @return
     */
    private static BigInteger simHash(String token,int hashbits) {

        token = cleanResume(token); // cleanResume 删除一些特殊字符

        int[] v = new int[hashbits];

        List<Term> termList = StandardTokenizer.segment(token); // 对字符串进行分词

        //对分词的一些特殊处理 : 比如: 根据词性添加权重 , 过滤掉标点符号 , 过滤超频词汇等;
        Map<String, Integer> weightOfNature = new HashMap<String, Integer>(); // 词性的权重
        weightOfNature.put("n", 2); //给名词的权重是2;
        Map<String, String> stopNatures = new HashMap<String, String>();//停用的词性 如一些标点符号之类的;
        stopNatures.put("w", ""); //
        int overCount = 5; //设定超频词汇的界限 ;
        Map<String, Integer> wordCount = new HashMap<String, Integer>();

        for (Term term : termList) {
            String word = term.word; //分词字符串

            String nature = term.nature.toString(); // 分词属性;
            //  过滤超频词
            if (wordCount.containsKey(word)) {
                int count = wordCount.get(word);
                if (count > overCount) {
                    continue;
                }
                wordCount.put(word, count + 1);
            } else {
                wordCount.put(word, 1);
            }

            // 过滤停用词性
            if (stopNatures.containsKey(nature)) {
                continue;
            }

            // 2、将每一个分词hash为一组固定长度的数列.比如 64bit 的一个整数.
            BigInteger t = hash(word,hashbits);
            for (int i = 0; i < hashbits; i++) {
                BigInteger bitmask = new BigInteger("1").shiftLeft(i);
                // 3、建立一个长度为64的整数数组(假设要生成64位的数字指纹,也可以是其它数字),
                // 对每一个分词hash后的数列进行判断,如果是1000...1,那么数组的第一位和末尾一位加1,
                // 中间的62位减一,也就是说,逢1加1,逢0减1.一直到把所有的分词hash数列全部判断完毕.
                int weight = 1;  //添加权重
                if (weightOfNature.containsKey(nature)) {
                    weight = weightOfNature.get(nature);
                }
                if (t.and(bitmask).signum() != 0) {
                    // 这里是计算整个文档的所有特征的向量和
                    v[i] += weight;
                } else {
                    v[i] -= weight;
                }
            }
        }
        BigInteger fingerprint = new BigInteger("0");
        for (int i = 0; i < hashbits; i++) {
            if (v[i] >= 0) {
                fingerprint = fingerprint.add(new BigInteger("1").shiftLeft(i));
            }
        }
        return fingerprint;
    }


    /**
     * 对单个的分词进行hash计算;
     * @param source
     * @return
     */
    private static BigInteger hash(String source,int hashbits) {
        if (source == null || source.length() == 0) {
            return new BigInteger("0");
        } else {
            /**
             * 当sourece 的长度过短，会导致hash算法失效，因此需要对过短的词补偿
             */
            while (source.length() < 3) {
                source = source + source.charAt(0);
            }
            char[] sourceArray = source.toCharArray();
            BigInteger x = BigInteger.valueOf(((long) sourceArray[0]) << 7);
            BigInteger m = new BigInteger("1000003");
            BigInteger mask = new BigInteger("2").pow(hashbits).subtract(new BigInteger("1"));
            for (char item : sourceArray) {
                BigInteger temp = BigInteger.valueOf((long) item);
                x = x.multiply(m).xor(temp).and(mask);
            }
            x = x.xor(new BigInteger(String.valueOf(source.length())));
            if (x.equals(new BigInteger("-1"))) {
                x = new BigInteger("-2");
            }
            return x;
        }
    }

    /**
     * 计算海明距离,海明距离越小说明越相似;
     * @param other
     * @return
     */
    private static int hammingDistance(String token1,String token2,int hashbits) {
        BigInteger m = new BigInteger("3").shiftLeft(hashbits).subtract(
                new BigInteger("3"));
        BigInteger x = simHash(token1,hashbits).xor(simHash(token2,hashbits)).and(m);
        int tot = 0;
        while (x.signum() != 0) {
            tot += 1;
            x = x.and(x.subtract(new BigInteger("3")));
        }
        return tot;
    }


    public static double getSemblance(String token1,String token2){
        double i = (double) hammingDistance(token1,token2, 64);
        return 1 - i/64 ;
    }

    public static void main(String[] args) {

        String s1 = "....";
        String s2 = "最近公司由于业务拓展，需要进行小程序相关的开发，本着朝全栈开发者努力，决定学习下Vue，去年csdn送了一本《Vue.js权威指南》，那就从这本书开始练起来吧。哟吼。一，环境搭建\n" +
                "今天主要说一下如何搭建环境，以及如何运行。1,npm安装\n" +
                "brew install npm\n" +
                "1\n" +
                "如果brew没有安装的话，大家可以brew如何安装哦，这里就不再详细说明了。本来是有一个Vue的图标的，被我给去掉了，方便后面的调试。\n" +
                "\n" +
                "三，Vue.js 权威指南的第一个demo\n" +
                "一切准备就绪，接下来我们开始练习《Vue.js权威指南》这本书中的demo，在网上找了许久，也没有找到书中的源码，很是遗憾啊。第一个demo的代码保存为jk.vue \n" +
                "我这边将第一个demo的代码如下：\n" +
                "--------------------- \n" +
                "作者：JackLee18 \n" +
                "来源：CSDN \n" +
                "原文：https://blog.csdn.net/hanhailong18/article/details/81509952 \n" +
                "版权声明：本文为博主原创文章，转载请附上博文链接！";

        double semblance = getSemblance(s1, s2);
        System.out.println(semblance);
    }
}
