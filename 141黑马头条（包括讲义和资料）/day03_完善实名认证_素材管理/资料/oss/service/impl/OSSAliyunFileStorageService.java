package com.heima.file.service.impl;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.heima.file.config.OssAliyunAutoConfig;
import com.heima.file.config.OssAliyunConfigProperties;
import com.heima.file.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
/**
 * @Description 阿里Oss文件上传
 */
@Slf4j
@EnableConfigurationProperties(OssAliyunConfigProperties.class)
@Import(OssAliyunAutoConfig.class)
@Primary  // @Autowired 类型
@Component("OSSAliyunFileStorageService")
public class OSSAliyunFileStorageService implements FileStorageService {
	@Autowired(required = false)  // 在启动时不检查Bean
			OSSClient ossClient;
	@Autowired
	OssAliyunConfigProperties ossAliyunConfigField;
	public String builderOssPath(String dirPath,String filename) {
		String separator = "/";
		StringBuilder stringBuilder = new StringBuilder(50);
		LocalDate localDate = LocalDate.now();
		String yeat = String.valueOf(localDate.getYear());
		stringBuilder.append(yeat).append(separator);
		String moth = String.valueOf(localDate.getMonthValue());
		stringBuilder.append(moth).append(separator);
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
		String day = formatter.format(localDate);
		stringBuilder.append(day.replace("-","")).append(separator);
		stringBuilder.append(filename);
		return dirPath+separator+stringBuilder.toString();
	}
	/**
	 * @param prefix      文件上传前缀
	 * @param filename    文件名称
	 * @param contentType 文件类型 "image/jpg" 或"text/html"
	 * @param inputStream 文件流
	 * @return pathUrl 全路径
	 * @Description 文件上传
	 */
	@Override
	public String store(String prefix, String filename, String contentType, InputStream inputStream) {
		return this.store(prefix, filename, inputStream);
	}

	/* (non-Javadoc)
	 * @see FileStorageService#store(java.lang.String, java.lang.String, java.io.InputStream)
	 */
	@Override
	public String store(String prefix, String filename, InputStream inputStream) {
		//文件读取路径
		String url = null;
		// 设置文件路径和名称（Key）
		String key = builderOssPath(prefix, filename);
		// 判断文件
		if (inputStream==null) {
			log.error("上传文件：key{}文件流为空",key);
			return url;
		}
		log.info("OSS文件上传开始：{}" ,key);
		try {
			String bucketName = ossAliyunConfigField.getBucketName();
			// 上传文件
			ObjectMetadata objectMeta = new ObjectMetadata();
			objectMeta.setContentEncoding("UTF-8");
			objectMeta.setContentType("image/jpg");
			// ossClient.putObject("hmleadnews", "material/a.jpg", inputStream);
			PutObjectRequest request = new PutObjectRequest(bucketName, key, inputStream,objectMeta);
			PutObjectResult result = ossClient.putObject(request);
			// 设置权限(公开读)
			ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
			if (result != null) {
				log.info("OSS文件上传成功：{}" ,key);
				url = key.toString();
			}
		} catch (OSSException oe) {
			log.error("OSS文件上传错误：{}", oe);
		} catch (ClientException ce) {
			log.error("OSS文件上传客户端错误：{}",ce);
		}
		return url;
	}

	/* (non-Javadoc)
	 * @see FileStorageService#delete(java.lang.String, java.lang.String)
	 */
	@Override
	public void delete(String pathUrl) {
		String key = pathUrl.replace(ossAliyunConfigField.getWebSite(),"");
		List<String> keys = Lists.newArrayList();
		keys.add(key);
		// 删除Objects
		ossClient.deleteObjects(new DeleteObjectsRequest(ossAliyunConfigField.getBucketName()).withKeys(keys));

	}

	@Override
	public void deleteBatch(List<String> pathUrls) {
		// 删除Objects
		ossClient.deleteObjects(new DeleteObjectsRequest(ossAliyunConfigField.getBucketName()).withKeys(pathUrls));
	}

	/* (non-Javadoc)
	 * @see FileStorageService#downloadFile(java.lang.String)
	 */
	@Override
	public InputStream downloadFile(String pathUrl) {
		// ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
		OSSObject ossObject = ossClient.getObject(ossAliyunConfigField.getBucketName(), pathUrl);
		InputStream inputStream =  ossObject.getObjectContent();
		return inputStream;
	}

	/* (non-Javadoc)
	 * @see FileStorageService#getFileContent(java.lang.String)
	 */
	@Override
	public String getFileContent(String pathUrl) throws IOException {
		InputStream inputStream = downloadFile(pathUrl);
		return new String(ByteStreams.toByteArray(inputStream));
	}

}
