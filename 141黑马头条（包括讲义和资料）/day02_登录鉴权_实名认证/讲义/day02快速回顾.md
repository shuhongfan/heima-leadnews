**常见的加密方式?**

```
可逆   加密 能够 解密
	对称
		加密 解密 使用相同的秘钥    算法透明 加密性能 速度快
	
	
	非对称
		分为公钥 私钥    如果公钥加密  私钥解密   
								加密安全性更高
不可逆
	MD5  加密 不能 解密，  如果密码简单会被暴力破解
	
	项目中采用 MD5 + salt (理解为随机生成10位的混淆字符串)
```

**什么是无状态登录?**

```
有状态:  用户登陆后服务端需要保存用户的登陆信息登陆状态，  session会话 

无状态:  用户登陆成功后服务端不保存用户的相关信息，只是给用户颁发凭证 token 
```



**什么是JWT Token?**

```
JSON WEB TOKEN  ：  一种token的标准化类型


header:  定义了 token的类型  token的加密方式  等等

payload:  存储token内容   如: 颁发时间   失效时间   用户登陆信息

signature: 签名信息，为防止token被篡改，   会将   加密算法(base64(header) + . + base64(payload))


```

**项目中是如何进行登录认证的?**

```
用户登陆:
   入参:  用户名  密码
   表:    ad_user  用户表
   步骤: 1. 根据用户名查询用户是否存在
        2.  对比密码是否一致   MD5 (输入的密码 + salt) 
        3.  判断用户状态  9 正常 
        4.  生成token   工具类:  jjwt 
        5.  将token和用户信息返回      data: {  token : {}   ,  user: {}  }

登陆鉴权:  
  通过定义网关 全局过滤器实现     实现 GlobalFilter     filter: 认证方法
  
        1.  查看请求路径是否属于白名单路径   (白名单: 不需要认证可以直接访问的路径 )
        2.  查看请求头中是否携带token信息  未携带直接终止请求 返回401
        3.  解析token  解析失败 返回401
        4.  解析成功  将token中存储的用户id 设置到请求头中 userId 路由给其它微服务
        5.  放行请求
        
        
浏览器: 
   在访问一个需要登陆的资源时， 没有登陆后端会返回401的状态
   
   浏览器将页面跳转到登陆页面   输入用户名密码登陆
   
   浏览器会将返回的token存储到浏览器中 localStorage(cookie   localStorage)
   
   下一次请求时，会将token设置到请求头中
        
```



**如何保证token令牌不被篡改?**

```
signature: 签名信息，为防止token被篡改，   会将   加密算法(base64(header) + . + base64(payload))
```



**token令牌能否保证数据安全?**

```
不能， token是作为登陆的一种凭证 

使用https   (http + SSL)

生成token  (存入: IP)

刷新token  
```

**请介绍下用户实名认证模块的业务流程？**

```
整体业务流程:
	app端用户 可以发起实名认证申请    (身份证照片   个人活体检测照片   手持身份证照片 )
	
	admin端管理   可以进行实名认证审核   如果审核通过: 会给app端的用户 开通一个自媒体账户 并且创建作者信息
	
	wemedia端    有了自媒体账户，就可以登陆自媒体端 ，   维护素材 粉丝  发表文章
	
表:    ap_user  app用户表    ap_user_realname 实名认证表(状态: 1 2 9)       user微服务
		wm_user 自媒体用户表                                             wemedia微服务
       ap_author 作者表                                                 article微服务
       
流程步骤:  
   1. 前端传入 实名认证id  加是否通过  
   
   2. 接收到参数后  检查参数:  id  实名认证信息   状态
   
   3. 修改实名认证的状态 :  (2 失败       9 通过)
   
   4. 如果是通过:
        4.1  远程的开通自媒体账户 
        
        4.2  远程的创建作者信息

```

**Feign的作用？**

```
feign: 声明式的HTTP客户端，  作用:  实现微服务间http的调用

```

**Hystrix的作用?**

```
限流:  

线程隔离:  

熔断降级:  

服务降级的方法:  
```

**项目中Feign的具体使用?**

```
在一个通用工程中引入feign的配置 
    @EnableFeignClients  开启feign的客户端扫描
    feign日志级别  FULL  (debug)
    
    想调哪个微服务:   创建接口
       // 代表是一个feign的客户端
       // value: 微服务的名称 代表要调用哪个微服务
    	@FeignClient(value="leadnews-wemedia"，fallbackfactory="服务降级对象",configuration = "配置" )
    	WemediaFeign
    	
    	
    	
    	WemediaFeignFallback 实现  FallbackFactory<WemediaFeign>、
    	    具体的服务降级方法
```



