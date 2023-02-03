##### day06_快速回顾



**发布文章流程**

```
自媒体人 在自媒体端发表文章后，会进行审核( 自动审核 8    人工审核 4) ，审核通过后 需要发布文章，根据文章的发布时间定时发布  发布后  会在article 数据库中，保存到3张表中， 并且修改wmNews的状态改为9 ，而且还有给发布成功的文章 生成静态页  


涉及表:    wm_news (1 --> 8 --> 4)  ==>  延时队列 ==>  ap_article   ap_article_config
                                                     ap_article_content
                                                     
                    9(已发布)             < ==    
```



**文章上下架同步**

```
自媒体端:  

  wm_news  (文章: 9  ,  enable 上架  下架)
  
  ==  mq  ==
  
  ap_article_config    (isDown  上架   下架)
```



**RabbitMQ实现延迟队列**

```
延时队列:  
                  消息(10s超时)                        
    死信队列:      发送者  ===>   交换机  ==>  队列 (没有消费者   死信交换机)
    						                             
    					       死信交换机  ==> 死信队列   ==>  消费者
    					       
    		      优点:  无需引入任何插件
    
    			  缺点:  需要创建更多的队列和交换机
    
                        如果消息的超时时间不一致，可能会有未超时的消息阻塞了超时的消息
            
    延时插件:      1.  安装延时插件   官网下载  安装包拷贝到插件目录  
    
    			 2.   创建交换机和队列   交换机一定要设置 delayed: true  
    			 
    			 3.   发消息时，携带消息头 header (x-delay , 延迟时间)
    			 
    			 优点:  实现简单,失效时间可以很灵活
    			 
    			 缺点:  引入插件  老版本不支持
    			 
```



**freemarker模板引擎**

```
模板引擎:  基于模板，使用数据替换模板变量，输出内容的一种技术
			
			模板  +  数据 = 输出

常见模板引擎:    jsp(servlet)    freemarker   thymeleaf   velocity (用的少)

常见使用场景:    web项目的视图
			   代码生成器
			   生成静态页
			   
			   
freemarker基础语法: 
          ${变量}
          
          ${map[key].properties}
          
          ${map.key.properties}
          
     <#指令 >     
     
     遍历集合:  <#list 集合 as 变量 >
     				${变量}
     			</#list>
     			
     判断:      <#if  表达式>
     				表达式为true时  内容显示
     			</#if>
     			
     内置函数  ${变量?xxx}
          ${变量??}  判断变量是否存在，返回boolean值
          
          ${集合变量?size}  集合的长度
          
          ${变量?date  time  datetime  string("yyyyMMdd")}
```



**minio 开源分布式对象存储服务**

```
OSS 对象存储服务

基于docker 部署的minio

使用步骤:
   1.  先创建bucket 存储空间
   
   2.  修改工具类 heima-file-starter 
         添加minio的实现类  FileStorageService
         
          FileStorageService     oss实现类 (@Primary 优先级高)
          
                                 minio实现类(bean=minio)
  3. 修改nacos中的配置文件  添加minio的配置
  
  4.  @Autowired
      @Qulifier(name="minio名称")
      FileStorageService minio对象
                        .store("哪个文件夹","文件的名称",文件内容类型,输入流)
```





