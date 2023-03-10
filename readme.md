## 黑马头条项目常见面试问题

### 项目背景相关问题

#### 1. 谈谈你们研发团队的组成是怎样的

**思路分析:**

```
判断是否有真实项目经验
不同公司规模不同，只要说的顺畅就可以
```

**回答样例：**

```
我们这个研发小组，一共16个人，后台java一共5个，前端两个，UI两个，测试两个
然后还有项目经理、架构师、产品、运维
```

#### 2. 谈谈你们项目的开发流程

**思路分析:**

```
判断是否有真实项目经验
项目的研发流程  如果回答的全一些，把整个项目的过程简单说下
具体说下前后端分离的开发过程
```

**回答样例：**

```
一般我们项目都是先立项，决定要做什么项目
产品会梳理需求，产生需求说明和原型
架构师根据原型设计架构、数据库
然后给我们分派任务，规划项目开发周期
具体开发时，我们现在都是按照前后端分离的模式来
	前端和后端一起根据业务原型 梳理业务需求
	根据业务需求定义接口文档
	前后端并行开发
	后端基于接口文档提供对应的接口实现, 单元测试  POSTMAN
    前端基于接口文档生成mock数据,进行前端的开发,按照mock数据进行测试
	功能完成后进行前后端联调 
	前端把访问地址改成后端服务的地址，进行真实数据的测试
	如果联调有问题，谁的问题谁去改

在进行联调功能测试，测试完毕即可发布到测试服务器，由测试人员进行测试

到时就改bug 在测

bug搞定后上线
```

#### 3. 接口文档是由谁来提供，什么格式?

**思路分析:**

```
怎么做的怎么答就可以
```

**回答样例：**

```
后端提供的，不过前期根据原型有和前端讨论过，后端引入swagger框架+knife4j框架生成的接口文档


请求路径    /api/v1/channel/list
请求方式:   get  post delete put
请求参数:   @RequestBody   @PathVariable  @RequestParam   
响应内容:   ResponseResult:
                 code   状态码   enum枚举维护状态码  0 成功   
                 message   提示信息
                 data    数据
```

#### 4. 项目数据库的设计，如果让你设计表该如何设计?

**思路分析:**

```
这是可发挥的题目, 如果准备的少，后面可以少说。不过问你能不能设计，尽量能设计

可以涉及到一些知识点
	表的设计规范(数据库三范式)
	mysql的数据库优化
	索引优化
```

**回答样例：**

```
我们项目是根据不同的微服务分了不同的数据库，表不到100张， 我们的数据库主要是由架构师设计的，

如果让我设计 我会根据原型梳理具体业务模块，根据业务模块间的关系梳理出不同表关系，然后在PowerDesigner上创建出对应的表 和 表字段 。 一些命名规则就根据公司的规范走，会注意满足数据库三范式，当然有时为了提高查询效率会创建对应的冗余字段，减少连接查询。字段选择合理的字段类型和范围，经常查询的字段需要考虑索引
```

#### 5. 项目代码是如何管理的？

**思路分析:**

```
考察之前项目的代码如何管理，需要准备使用git的大体流程

clone指定分支代码:
git clone -b  指定分支  Git地址

```

**回答样例：**

```
我们之前代码都是统一git管理的，公司服务器部署了GitLab, 每个人都有账号密码，从git上clone dev分支代码到本地, 功能开发后会将代码先pull更新，在提交到git开发分支，如果要有冲突，解决冲突部分然后通过merge命令进行合并， 我们项目尽力在功能完成时会将dev分支 合并到测试分支，然后发布一版进行功能测试
```

#### 6. 你们的项目做了7个月，这7个月都做了哪些事情

**思路分析:**

```
结合项目时间来，不用太精确 大概说下就好。

提到到设计数据库
设计接口可能会延伸对应的知识点
```

**回答样例：**

```
从项目立项后，就会讨论需求、研究原型、设计数据库、分任务，设计接口这一段大概用了1个月左右，具体开发2个多月，然后修改bug和需求调整用了很长一段时间
```

#### 7. 黑马头条项目的盈利模式?

**思路分析:**

```
如果你说一个线上项目，注意看app中的广告有没有 详情中有没有打赏， 底部菜单能跳转到哪些其它功能上，对应着说
```

**回答样例：**

```
主要赢利点 
app广告: 运营端会有广告管理模块，可以在app的不同位置，翻看的文章中有对应广告位
赞赏功能: 我们文章都是技术型文章，文章详情页有打赏功能， 平台会抽取一部分
网站导流: 公司还有其它业务，通过下面菜单栏可以进入到我们公司的电商网站中
```

#### 8. 你们项目上线了，每天的PV，QPS，TPS，注册用户数都是多少

**思路分析:**

```
项目上线，运营人员会有这些运营数据指标， 数量多少都不一定，看你app的火爆程度

常见指标:
PV：page view 页面访问量
QPS：Query Per Second 接口 每秒查询数量
TPS: Transaction Per Second 接口 每秒事务处理数量,这个值会比QPS小很多
注册用户数量 一般指的是网站运营到现在，有多少注册用户


如: 每天下载量 4000~6000次 留存率: 40%左右  每天新增注册人数: 1600~2400
	项目运营 3个月 
	注册人数: 3*30*2000 大约 180000
	但是大部分用于注册完 活跃度会变低，所以每天活跃用户能剩余%10就不错了
	每个人查看文章 10个文章
	每天的PV = 18000 * 3 * 10 = 大约500000PV/天
	QPS不会太高，按均值算 100左右  但是如果微服务搭建集群QPS能做到2000左右
	
	结合上面的数值不太离谱就好
```

**回答样例：**

```
我们项目现在刚运营不久，注册量有10多万吧， QPS不是很高100左右， 不过在用jmeter测试时，接口QPS能做到1000多，TPS没有专门测试过。
```

### 项目及通用技术相关问题

#### 1. 请介绍一下你们最近做的项目?

**思路分析:**

```
项目背景: 简单表达出项目是做什么的?
项目用到的技术: 可以顺着部署架构来说
项目中的角色和负责模块: 简单说下做过的模块，使用到的亮点技术就好，不用细说
介绍完毕后，和面试官要有沟通，询问下要介绍下哪个模块
或者看看面试官有没有提问的欲望，没有主动介绍自己准备好的模块
```

**回答样例：**

```
我最近做的项目 是黑马头条APP的资讯类项目，有点类似于今日头条， 现在人们都离不开手机，所以这种阅读类的抢占用户碎片化时间的app很火，我们项目是采用前后端分离的模式开发的，前端分为两个PC管理web端，还有app端，app端采用阿里的Weex框架,可以提供h5 andriod和ios的发布方案，前端整体采用Vue的相关技术，  后端主要使用SpringBoot + SpringCloud为主的微服务架构，大体架构是nginx接收用户请求，反向代理到网关 网关使用的是SpringCloudGateway, 然后注册中心 和 配置中心用的都是 alibaba的nacos,
微服务中的配置都会统一存放在nacos中， 所有微服务提供的服务地址也会存储在nacos中
服务与服务之间使用Feign的Http客户端调用，feign整合了Ribbon负载均衡器，Hystrix熔断器， 另外还用到了alibaba的seata处理分布式事务，xxljob处理分布式调度，oss处理分布式的存储， 数据方面：核心数据存到了mysql中 ， 用redis做的缓存， 用到了es做搜索，还有mongodb、kafka等软件， 我在项目中主要复制后台微服务接口开发，负责文章的相关功能， 比如: 发表文章、文章的自动审核、文章的定时发布、热点文章的计算， 还有app端的文章展示评论等等功能 
```

#### 2. 项目中web接口的返回结果是如何定义的?

**思路分析:**

```
不同公司封装的通用结果类名可能不一样，但返回的字段大致相同
所以 只说ResponseResult里面的属性字段说出来就行
```

**回答样例：**

```
我们封装了通用的响应结果，
主要有:  
	code 状态码  
	message 提示信息 
	data 具体响应给前端的数据
```

#### 3. 项目中的异常是如何处理的?

**思路分析:**

```
主要两个注解:
@ControllerAdvice
@ExceptionHandler
```

**回答样例：**

```
我们定义了通用异常的处理类, 使用了@ControllerAdivice 控制类增强注解标注，
配合@ExceptionHandler 异常处理注解，该注解需要指定异常类型，当controller中抛出对应类型异常时，这个方法就被执行了，我们可以在通用类中定义多种不同异常，进行对应的统一响应结果返回


在黑马头条中  一共捕获3种异常：

		Exception.class 不可预知异常， 返回通用错误响应   服务器出现异常，请稍后重试
		
		CustomException.class 可预知异常，返回声明的异常信息， 如: 未查询到相关用户信息
		
		MethodArgsNotValidException.class 方法参数校验异常，用于参数校验不合规，返回提示
					如:  @NotNull(message="id不能为空")
						Integer  id ;
						
				        如果用户未传id: 进行对应提示
```

#### 4. 项目中的权限功能是如何实现的?

**思路分析:**

```
头条课程只做了 登录及登录鉴权
如果想做admin运营端功能权限管理, 如不同的管理员用户拥有不同的权限，可以访问不同的功能，还需要结合 传智健康中的 Springboot+Security 权限管理
权限系统学习资料:
链接：https://pan.baidu.com/s/1O8GXwtb96asNI5-kpXxF6Q 
提取码：hh1b 
```

**回答样例：**

```
登录鉴权我们用的是JWT token的无状态登录， 用户访问时请求header中携带token令牌，过滤器中会解析该token,如果没登录可以调用登录方法，登陆成功后会颁发token, 运营管理端实现了功能权限管理功能，主要是基于Springboot+SpringSecurity实现的，可以实现到按钮级别的权限控制。
```

#### 5. 项目中的日志是如何处理的?

**思路分析:**

```
单体项目 只要引用日志框架就可以了
微服务分布式项目，需要分布式的日志解决方案
```

**回答样例：**

```
我们每个微服务的日志都是通过logback日志框架打印的，比如重要方法的入参，报错后信息的打印，分布式日志我们用的是ELK的日志方案，微服务将日志通过kafka异步输入到logstash中，logstash将日志数据转发到elasticsearch中，然后通过kibana进行日志的搜索及查看，可以按照微服务名搜索，可以按照级别搜索 等等

另外，也部署了skywalking进行链路追踪信息的查看
```

#### 6. 项目的部署流程是怎样的?用了几台服务器?

**思路分析:**

```
一般公司有运维负责部署
开发可以不用清楚具体服务器信息
所以体现出 和我们相关的部署流程即可
```

**回答样例：**

```
具体几台服务器，我不太清楚，有专门的运维负责，不过部署流程采用的是持续集成的方式，
开发将代码上传到git, git会触发持续集成jenkins进行任务构建 构建是基于docker构建镜像，并推送到指定镜像中心，然后在触发rancher进行服务部署，这种持续集成的方案。

不过大概的部署架构我了解一些，接入层采用nginx 接收所有用户请求，在nginx中部署前端的静态资源，后台微服务网关的访问也是通过nginx, 配置的反向代理。 微服务架构中部署nacos作为注册中心，配置中心。 然后每一个微服务也都是构建成docker镜像，通过docker来部署的。
```

#### 7. 是否在生产环境下排查过bug? 如何排查?

**思路分析:**

```
项目部署到生成环境了，就不能在本地控制去看了
所以需要结合其它的手段来解决
考察生产环境解决问题的能力
```

**回答样例：**

```
解决过，一般问题都过打印的日志能看出大概问题是什么，日志我们用elk的架构解决的，从kibana中可以查看level为error级别的日志，看我们打印的错误信息， 如果日志体现不出来，也可以通过skywalking查看链路追踪数据，看报错的调用链 是哪一个环境出现的问题
```

### 登录鉴权功能介绍

#### 1. 功能模块介绍

**思路分析:**

```
未登录怎么拦截
如何登录
拦截通过后用户信息的获取 为思路进行操作

二级面试点: 验证流程细节   JWT相关
```

**回答样例：**

```
如果用户访问需要登录才能操作的uri,在网关过滤器中会对请求进行拦截，会对请求头中的token信息进行解析， 如果: token不存在、失效、篡改 都会返回401状态值， 前端工程会调用登录接口进行认证，可以通过用户名密码，也可以进行验证码登录，密码我们是MD5+salt的方式存储的，如果根据用户名查询到用户并且密码一致，使用jwtUtils构建token, 将用户的ID也生成到token中。 前端将token存到LocalStorage中，再次访问请求头会携带token, 这样就可以网关的过滤器就会通过，通过后将用户信息通过请求头携带到用户微服务，用户微服务使用过滤器将用户信息存储到ThreadLocal中，在需要获取用户信息的地方通过线程局部变量ThreadLocal来获取用户信息。
```

#### 2. 能详细介绍一下JWT技术么

**思路分析:**

```
1. 说下无状态登录

2. JWT的构成

3. 注意它解决的是认证 不是 安全
```

**回答样例：**

```
jwt是一种token技术，用于实现无状态登录，以前我们登录都是把登录信息存储在session中保存，而使用token是通过一些加密编码的算法将信息存储在凭证中，用户只需要带着凭证来，后台服务只需要对它进行解析即可。

JWT生成的token主要由三部分组成， 第一部分是header部分 主要存储jwt的类型和使用哪种加密算法， 第二部分是payload 载荷部分，用于存储token中要存储的信息，比如登录后的用户信息，失效时间等等。。 不过这里不适合存储敏感信息，因为头部分 和 载荷部分都是将对应的json内容通过base64编码的，有可能会被解析。 第三部分是签名信息，内容是前两部分json拼接到一起，根据指定类型的加密算法 结合 后台存储的密钥进行加密后的签名字符串，用于防止token的内容被篡改

```

#### 3. token在前端存放哪里，如果token被窃取了怎么办

**思路分析:**

```
注意: token 解决的是认证不是安全
```

**回答样例：**

```
token在前端存储到localStorage中，如果token被窃取了可能会用这个凭证登录我们的系统，所以使用token时 最好 配合https安全连接，尽量避免token被窃取。		
```

#### 用户实名认证审核模块

#### 1. 功能模块介绍

**思路分析:**

```
APP端提交实名认证 ==>  Admin端审核实名认证 ==> 通过 ==> 同步信息到自媒体用户表
                                                   同步信息到作者表
                                          不通过 ==> 更改审核状态失败
                                          
 二级面试点: 用友云实名认证接口   分布式事务
```

**回答样例：**

```
在我们头条项目中，采用不同的微服务对应不同的库，对于APP端 自媒体端都有不同的用户表，如果app的访问用户想在自媒体平台发布文章需要先进行实名认证， 
用户在APP端填写认证信息  如: 手机号  身份证号  姓名，还有会上传个人的手持身份证正反面的相关照片，照片我们存储在fastDFS中， 

在admin端的审核接口中我们使用用友云的实名认证接口检查用户的身份证 姓名 手机号是否真实有效，照片是否符合要求，如果审核通过将该app用户的信息同步到自媒体对应的user表 及 生成作者信息， 这里面在处理时涉及分布式事务，我们项目中的分布式事务统一使用alibaba的seata框架进行处理的，它支持多种模式的事务处理，我们主要使用的是它里面的AT模式。 具体使用只需要在事务的发起方法中添加@GlobalTrasactional 注解即可。
```

### 媒体素材管理

#### 1. 功能模块介绍

**思路分析:**

```
分布式存储
```

**回答样例：**

```
自媒体的素材模块，是自媒体作者可以维护自己的常用素材，将素材上传到OSS中，可以对上传的素材进行管理，当我们发表文章时可以直接选择这些素材进行快捷性的文章发布
```

### 发表文章

#### 1. 功能模块介绍

**思路分析:**

```
二级面试点:
	文章相关状态
	RabbitMQ相关
```

**回答样例：**

```
在自媒体端的发表文章模块，编辑文章标题 内容 标签 选择频道 自动发布时间 还有封面图片 封装成一个大的json调用后台的发布文章接口，后台对数据进行处理 比如:添加文章信息，抽取内容 和 封面中所涉及的所有图片路径 和 素材库的图片做对比，然后设置好关联关系， 如果是自动生成封面的话 还会根据内容中包含图片的格式，按照规则自动生成封面图片，这里面封面图片在app中显示时是非常小的图片所以这里我们会对图片进行压缩，压缩成128*128的小图，专门用于在列表中显示  文章保存成功后 会通过RabbitMQ向admin微服务发送一条待审核文章信息。admin微服务收到这个消息后就会自动调用自动审核的方法
```

### 文章自动审核功能

#### 1. 功能模块介绍

**思路分析:**

```
二级面试点:
	文章状态
	阿里云内容安全
	DFA算法
	redis
	mysql优化(主从 或  分库分表)
	AOP
```

**回答样例：**

```
admin微服务会监听指定主题的消息，在消息中会得到待审核的文章ID admin端使用Feign远程获取文章的具体信息，检查文章的各种状态 (0 草稿 1 待审 2 审核失败 3 人工审核 4 人工审核通过 8 通过待发布 9 发布)，如果是 审核通过的需要判断下发布时间是否小于等于当前时间，如果是的话直接发布就可以了， 如果是待审核需要对文章进行审核操作，审核
对于资讯类项目很关键，我们选用的是阿里云内容安全服务，可以检测文本、图片、视频等等， 根据阿里云的API 检测的结果大体上分三种，1. 通过 2. 需人工审核 3. 违规，根据不同的结果修改文章的审核状态，我们也自己定义了敏感词库处理我们自己微服务的敏感词，自定义的敏感词我们是通过实现DFA算法实现的敏感词过滤，敏感词的列表缓存到了Redis中。 如果阿里云和自定义的敏感词全部审核通过了，那么修改文章审核通过状态8  如果到达的发布时间，就直接发布文章  发布文章要把文章的信息同步到article库中，app端对article的相关信息都是查询article库， 文章在这里也被拆分成了多张表  
比如: 文章基本信息表，文章内容表，文章配置表
而且这个库我们还使用mysql主从的方式作为了优化，后台会通过aop智能的切换主库 从库
```

### 定时发布文章处理

#### 1. 功能模块介绍

**思路分析:**

```
xxl-job 分布式调度
```

**回答样例：**

```
头条中发布的文章是支持定时发布的，我们发布文章时可以指定某一个时间点发布，项目中我们引用了xxl-job框架实现分布式调度任务，具体使用需要单独部署调度中心，在调度中心中创建执行器和调度任务，在调度任务中定义cron日期表达式 每分钟执行一次( 0 0/1 * * * ?) , 在admin微服务中引入xxljob配置 对应的定时方法中 去文章库查询所有审核通过(4或8) 且 发布时间小于等于当前时间的待发布文章，然后调用文章发布的方法发布文章

(这里面的发布文章，也可以使用线程池优化)
```

### APP端文章详情数据展示

#### 1. 功能模块介绍

**思路分析:**

```
二级面试点:
热点文章处理
雪花ID算法
```

**回答样例：**

```
主要是app端各类数据的展示，并不是特别难，比如文章列表查询，查询article库中未下架和未删除的文章列表， 根据ID查询关联的文章内容，等等查询功能。不过这里面因为我们的文章ID用的是雪花ID算法生成的long类型数值，在展示详情时出现了精度丢失的问题，不过解决也很简单，在article的实体类中加入了序列化@JsonSeries注解，将long序列化成String,就可以了。   另外文章列表查询时 ，第一页查询的都是热点文章，热点文章我们是存储在redis中的。
```

### 文章评论功能

#### 1. 功能模块介绍

**思路分析:**

```

```

**回答样例：**

```

```

### 文章搜索实现

#### 1. 功能模块介绍

**思路分析:**

```

```

**回答样例：**

```

```

### 热点文章的处理方案

#### 1. 功能模块介绍

**思路分析:**

```
知识点: 
   rabbitMQ
   Redis + List + Lua
   xxl-job
   JDK Stream
```

**回答样例：**

```

```

### 粉丝管理模块

```
知识点:
	redis 中 zset
	mongoDB 
	
	echartjs
```



```
后台媒体端的粉丝统计，可以基于echarts图表的各种统计功能，如统计 今日 本周 近7天 近30天 粉丝的对于自己文章的阅读量，评论量，收藏量。 查询的数据最终通过折线图、柱状图进行展示，通过饼状图显示粉丝的性别、粉丝的年龄区间的数量等等，还可以查询粉丝列表私信和关注功能
```

### 数据统计分析模块

```
知识点: 
   rabbitMQ
   Redis + List + Lua
   xxl-job
   JDK Stream
   mongoDB
   
   
   前端 echartjs (柱状图 饼状图 折线图)
```



```
根据时间段，统计自己发布过的所有文章数量，评论量，收藏量，阅读量等等数据柱状图
```

### 自媒体评论管理模块

```
查询自媒体人发布的文章的相关最新评论，可以通过该评论列表快捷的对评论进行回复，点赞，删除，置顶举报 等等操作
```



### 批量导入导出

```
知识点: 
    mybatis批处理
    redis 去重
    easy excel
```



### 日志处理

```
知识点:
    elk 分布式日志
    
    aop 操作日志
    
    skywalking  应用性能监控平台 (链路日志)
```



