package com.heima.file.config;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
@Setter
@Getter
@NoArgsConstructor
@ToString
@ConfigurationProperties(prefix = "file.oss")  // 文件上传 配置前缀file.oss
public class OssAliyunConfigProperties implements Serializable {
    /***
     * 站点
     *
     */
    private String webSite;

    /**
     * 域名站点
     */
    private String endpoint ;

    /**
     * 秘钥Id
     */
    private String accessKeyId ;

    /**
     * 秘钥
     */
    private String accessKeySecret ;

    /**
     * 桶名称
     */
    private String bucketName ;

    /**
     * 设置OSSClient允许打开的最大HTTP连接数，默认为1024个
     */
    private Integer maxConnections ;

    /**
     * 设置Socket层传输数据的超时时间，默认为50000毫秒
     */
    private Integer socketTimeout;

    /**
     * 设置建立连接的超时时间，默认为50000毫秒
     */
    private Integer connectionTimeout;

    /**
     * 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒。
     */
    private Integer connectionRequestTimeout ;

    /**
     * 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒。
     */
    private Integer idleConnectionTime ;

    /**
     * 设置失败请求重试次数，默认为3次。
     */
    private Integer maxErrorRetry ;

    /**
     * 设置用户代理，指HTTP的User-Agent头，默认为aliyun-sdk-java。
     */
    private String userAgent ;

    /**
     * 代理端口
     */
    private String proxyHost;

    /**
     * 代理账号
     */
    private String proxyUsername;

    /**
     * 代理密码
     */
    private String proxyPassword;

    /**
     * 白名单
     */
    private String whiteList;



}
