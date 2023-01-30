package com.heima.file.config;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.comm.Protocol;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.SetBucketLoggingRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/***
 * @description Oss文件存储服务器
 * @return
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({OssAliyunConfigProperties.class})
//当引入FileStorageService接口时
@ConditionalOnClass({OSSClient.class})
public class OssAliyunAutoConfig {
	@Autowired
	OssAliyunConfigProperties ossAliyunConfigField;
	@Bean
//	@ConditionalOnMissingBean(OSSAliyunFileStorageService.class)
	public ClientConfiguration clientConfiguration() {
		// 创建ClientConfiguration。ClientConfiguration是OSSClient的配置类，可配置代理、连接超时、最大连接数等参数。
		ClientConfiguration conf = new ClientConfiguration();
		// 设置OSSClient允许打开的最大HTTP连接数，默认为1024个。
		conf.setMaxConnections(ossAliyunConfigField.getMaxConnections());
		// 设置Socket层传输数据的超时时间，默认为50000毫秒。
		conf.setSocketTimeout(ossAliyunConfigField.getSocketTimeout());
		// 设置建立连接的超时时间，默认为50000毫秒。
		conf.setConnectionTimeout(ossAliyunConfigField.getConnectionTimeout());
		// 设置从连接池中获取连接的超时时间（单位：毫秒），默认不超时。
		conf.setConnectionRequestTimeout(ossAliyunConfigField.getConnectionRequestTimeout());
		// 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒。
		conf.setIdleConnectionTime(ossAliyunConfigField.getIdleConnectionTime());
		// 设置失败请求重试次数，默认为3次。
		conf.setMaxErrorRetry(ossAliyunConfigField.getMaxErrorRetry());
		// 设置是否开启二级域名的访问方式，默认不开启。
		conf.setSLDEnabled(false);
		// 设置连接OSS所使用的协议（HTTP/HTTPS），默认为HTTP。
		conf.setProtocol(Protocol.HTTP);
		// 设置用户代理，指HTTP的User-Agent头，默认为aliyun-sdk-java。
		conf.setUserAgent(ossAliyunConfigField.getUserAgent());
//    	// 设置是否支持将自定义域名作为Endpoint，默认支持。
//    	conf.setSupportCname(false);
//    	// 设置代理服务器端口。
//    	conf.setProxyHost(ossAliyunConfigField.getProxyHost());
//    	// 设置代理服务器验证的用户名。
//    	conf.setProxyUsername(ossAliyunConfigField.getProxyUsername());
//    	// 设置代理服务器验证的密码。
//    	conf.setProxyPassword(ossAliyunConfigField.getProxyPassword());
		return conf;
	}

	@Bean
	@ConditionalOnBean(ClientConfiguration.class)
	public OSSClient ossClient(){
		log.info("-----------------开始创建OSSClient--------------------");
		OSSClient ossClient = new OSSClient(ossAliyunConfigField.getEndpoint(), ossAliyunConfigField.getAccessKeyId(), ossAliyunConfigField.getAccessKeySecret(), clientConfiguration());
		//判断容器是否存在,不存在就创建
		if (!ossClient.doesBucketExist(ossAliyunConfigField.getBucketName())) {
			ossClient.createBucket(ossAliyunConfigField.getBucketName());
			CreateBucketRequest createBucketRequest = new CreateBucketRequest(ossAliyunConfigField.getBucketName());
			//设置问公共可读
			createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
			ossClient.createBucket(createBucketRequest);
		}

//		//添加访问白名单
//		List<String> refererList = Arrays.asList(whiteList.split(","));
//		// 添加Referer白名单。Referer参数支持通配符星号（*）和问号（？）。
//		// 设置存储空间Referer列表。设为true表示Referer字段允许为空。
//		BucketReferer br = new BucketReferer(true, refererList);
//		ossClient.setBucketReferer(ossAliyunConfigField.getBucketName(), br);

		//添加客户端访问日志
		SetBucketLoggingRequest request = new SetBucketLoggingRequest(ossAliyunConfigField.getBucketName());
		// 设置存放日志文件的存储空间。
		request.setTargetBucket(ossAliyunConfigField.getBucketName());
		// 设置日志文件存放的目录。
		request.setTargetPrefix(ossAliyunConfigField.getBucketName());
		ossClient.setBucketLogging(request);

		log.info("-----------------结束创建OSSClient--------------------");
		return ossClient;
	}


}
