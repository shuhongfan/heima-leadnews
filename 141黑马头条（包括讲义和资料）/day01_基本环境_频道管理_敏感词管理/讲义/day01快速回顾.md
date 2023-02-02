**请简单介绍一下你们的项目?**

```
新闻   资讯   头条

ToC   面向互联网用户      ToB 面向企业的项目

运营管理端: 管理员登陆这个系统管理相关内容

自媒体端: 自媒体可以在这个系统 发布文章 管理粉丝  管理素材

app端: 互联网用户可以在app端查看最新 最热门的资讯
```



**你们项目的技术架构是怎样的?**

```
采用微服务架构搭建

SpringBoot + SpringCloud (nacos  feign  ribbon  gateway seata sentinel hystrix )

Spring  SpringMVC  mybatisPlus


```

**你们Springboot及SpringCloud使用的版本?**

```
springboot 2.3.9

springcloud  Hoxton.SR8
```

**你是如何理解前后端分离开发的?**

```
1. 前端后端 根据需求 整理出接口文档

2. 前端后端并行开发

	后端基于微服务 基于接口文档 提供接口，提供完毕后 通过postman进行接口测试
	
	前端实现页面效果， 基于mock框架 基于接口文档生成模拟数据，进行页面调试
3. 前后端进行联调
	前端将访问地址改为后台网关地址  使用真实数据进行测试
	
	如果出现bug  谁的问题谁去改
	
4. 项目上线
```

**你们后端接口是如何测试的?**

```
postman进行测试

swagger + knife4j 通过API框架 生成接口文档 ，进行在线调试
```

**SpringMVC接收参数的注解有哪些?**    

```

@RequestBody    POST  xxxxx                       @RequestBody User user
                {
                    name: "",
                    age : 18
                }
@PathVariable   GET /user/101               @GetMapping(/user/{id})  
                                                   @PathVariable("id") Integer id

@RequestParam    GET /user/101?age=10&name=xiaoming
 											@GetMapping(/user/{id})  
                                                   @PathVariable("id") Integer id
                                                   @RequestParam Integer age
													@RequestParam String name
```

**你们的接口文档是如何定义的**: 

```
接口文档: 
      接口描述
      
   1. 请求方式  GET POST DELETE PUT
   
   2. 请求路径  /user/{id}
   
   3. 请求参数  
   
   4. 响应内容  
   
  Swagger + knife4j  生成的
  
  		@Api  描述controller类的作用
  		
  		@ApiOperation  描述controller类中方法的作用，web接口
  		
  		@ApiModelProperties  描述属性的作用
```

**你们项目的数据库设计情况？ 你是否有独立设计过数据库?**

```
微服务架构  按照业务拆分的数据库

admin微服务 ==>   admin数据库

wemedia微服务  ==>  wemedia 自媒体库

article微服务 ==>  article 文章库

user微服务 ==>  user 用户库

冗余的设计: 

    微服务 为了降低关联查询  在表会添加冗余字段
    
             user      order
                        userId
                        name  冗余字段
```

**能否说出SpringBoot的自动装配原理?**

```

@SpringBootApplication
    @SpringBootConfiguration  启动类也是一个配置类
    
    @ComponentScan 组件 默认扫描启动类所在的包
    
    @EnableAutoConfiguration  开启自动状态
    
    	@AutoConfigurationPackage  自动配置当前工程 相关组件
    	
		@Import(AutoConfigurationImportSelector.class)
			查询相关依赖jar包 ==>  META-INF/spring.factories ==> 读取key为EnableAutoConfiguration的所有配置类的全限定类名:
			
			上面读到的类  都是候选配置，有非常多   
				
				1. 去除重复的配置
				
				2. 去除手动排除的配置
				
				3. 过滤掉不满足条件的配置    @ConditionalOnXXX 条件
			
			剩余的配置  就是要加载的配置类


```

**项目中是否自定义过starter起步依赖，如何定义?**

```
定义过

统一异常

统一接口文档

写好配置类

准备好META-INF/spring.factories 就可以了  
```

**项目中异常是怎么处理的?**

```
采用全局异常处理

	@ControllerAdvice   controller的增强注解
	
	@ExceptionHandler   异常处理器
	
具体步骤:
    1. 声明一个类  类上添加 @ControllerAdvice
    
    2. 类中创建方法  方法上添加 @ExceptionHandler(Exception.class)
    
    3. 方法中返回默认的 ResponseResult 统一错误的结果
```

**项目中注册中心的作用?**

```
注册中心 nacos

服务注册

服务发现

服务状态监控

配置中心 nacos

配置的统一管理

共享配置:  配置中通过 shared-configs 配置共享配置
```

**项目中api网关的作用?**

```
微服务的统一入口，可以实现 路由    鉴权     限流   过滤
```

**如何进行前后端联调?**

```
F12  ==>  network  ==> 请求路径  ==> 请求参数  ==>  响应状态  ==> 响应结果

后端   Debug 一步步排查
```



