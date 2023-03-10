**文章自动审核流程(业务说明，具体实现)**

```
文章状态:  0 草稿  1 待审核  2 未通过  3 人工审核     4 人工审核通过  8 自动审核通过  9 已发布  


1. 发表文章时，如果文章状态为 1 (待审核) ,发送mq消息  触发自动审核


2. 消费者接收到消息后，触发自动审核方法 (参数 : 文章id)

    2.1  根据文章id查询自媒体文章
    
    2.2  判断状态是否  是  1 
    
    2.3  基于DFA算法进行敏感词审核     未通过  状态: 2   通过 继续下一步
    
    2.4  基于阿里云内容安全检测，检测文本    未通过  状态: 2  不确定: 状态: 3   通过 继续下一步

	2.5  基于阿里云内容安全检测，检测图片    未通过  状态: 2  不确定: 状态: 3   通过 继续下一步

3. 如果都通过  将文章状态修改为 8 

4、 TODO   定时发布文章
```

**文章人工审核流程(业务说明)**

```
管理员 可以登陆 admin运营端  对文章进行审核

	通过:  4
	
	不通过:  2
```

**阿里云内容安全服务介绍及使用**

```
阿里云内容安全服务，可以检测:  文本内容、图片、音频、视频、图文  是否包含违规信息

1. 阿里云开通内容安全服务

2. 申请key和秘钥

3. 结合官方SDK的DEMO 进行使用 (文本   图片)
		文本:   场景:  文本反垃圾
		
			   响应内容:   suggestion  建议:  pass  review  block
			   
			   			  label      原因: 为什么没通过
			   			  
			   			  filteredContent   将违规内容替换成** 后的文本 
			   			  
        图片:   场景:  鉴黄  暴恐  涉政  广告
        
        	   响应内容:   suggestion  建议:  pass  review  block
			   
			   			  label      原因: 为什么没通过

4. 基于官方demo封装工具类，创建starter依赖


```

**DFA算法介绍及使用**

```
1. admin端  会微服务敏感词信息，存储到敏感词表中

2. 基于DFA算法来检测，内容是否包含敏感词

	将敏感词集合  封装成一个  大的map 套着 若干 小的map   敏感词的每个字都是map中的key, 每个字又对应一个map的value, 对应的map中的key 是这个字的下一个字，  而且每个字都有一个 isEnd状态字段, 如果isEnd的值 是 1的话，代表是敏感词的最后一个字
	
	检测内容是否包含敏感词时， 遍历内容中的每一个字，去map中查询  
```

**MQ介绍及常见应用场景**

```
message queue  消息队列，  提供应用间异步通信的软件


解耦

异步

消息通知

流量削峰 

```

**AMQP消息协议核心概念介绍** 

```
producer  生产者

broker  mq的server端

exchange  交换机 

binding  绑定

queue  队列

consumer  消费者
```

**RabbitMQ支持的消息模式**

```
简单模式       p ==>  queue ==>  c

work模式      p ==>  queue ==>  c     默认平均消费:  可以通过 prefetch 1  改为能者多劳模式
 
                               c
                               
发布订阅模式:   direct   fanout   topic


direct 路由模式:    p ==>  exchange   red    queue1      c

                                    blue    queue2     c
                                    
                                    red     queue3     c

fanout 广播模式:   p ==>  exchange       queue1      c

                                        queue2     c
                                    
                                        queue3     c
topic 主题模式:    p ==>  exchange   red.#     queue1      c

                                    blue.*    queue2     c
                                    
                                    red     queue3     c
                                        
```

**RabbitMQ如何保证高可用**

```
普通集群:   不够高可用

镜像集群:   主从模式    一致性不够强

3.8 ==> 仲裁队列:  高可用  并且保证一致性
```

**RabbitMQ如何保证消息的可靠性(或 消息不丢失)**

```
发消息到mq
- 		**发送确认机制**  

- 		**消息返还机制**

mq如何保证不丢失
- 		**持久化机制**


消费者消费消息
-         **消息确认机制**
			none  可能会丢消息
			
			manual   手动   
			
			auto   自动     无限重试
-         **消息重试机制**

```

**如何保证消息避免重复消费(保证幂等性)**

```
1. 接收消息，通过消息的主键  根据数据库里数据的状态进行判定
	自动审核:   newsId  ==>  wm_news ==> status ==> 1
	
2. 基于redis, 缓存消息处理记录，来避免消息重复消费
```

**积压上百万消息如何快速消费**

```
1. 增加多个消费者  目的: 提升消费消息的速度

2. 可以在消费者中，引入线程池  提升消费速度

3. 将普通队列 改成 惰性队列  能存储更大量的消息，并且  性能比较稳定
```

