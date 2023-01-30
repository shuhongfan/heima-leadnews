## 1 MongoDB相关概念

![image-20210714215717065](assets/image-20210714215717065.png)

### 1.1 业务应用场景

传统的关系型数据库（如MySQL），在数据操作的“三高”需求以及应对Web2.0的网站需求面前，显得力不从心。

解释：“三高”需求：

• High performance - 对数据库高并发读写的需求。

• Huge Storage - 对海量数据的高效率存储和访问的需求。

• High Scalability && High Availability- 对数据库的高可扩展性和高可用性的需求。

**而MongoDB可应对“三高”需求。**



具体的应用场景如：

1）社交场景，使用 MongoDB 存储存储用户信息，以及用户发表的朋友圈信息，通过地理位置索引实现附近的人、地点等功能。

2）游戏场景，使用 MongoDB 存储游戏用户信息，用户的装备、积分等直接以内嵌文档的形式存储，方便查询、高效率存储和访问。

3）物流场景，使用 MongoDB 存储订单信息，订单状态在运送过程中会不断更新，以 MongoDB 内嵌数组的形式来存储，一次查询就能将订单所有的变更读取出来。

4）物联网场景，使用 MongoDB 存储所有接入的智能设备信息，以及设备汇报的日志信息，并对这些信息进行多维度的分析。

5）视频直播，使用 MongoDB 存储用户信息、点赞互动信息等。



这些应用场景中，数据操作方面的共同特点是：

（1）数据量大

（2）写入操作频繁（读写都很频繁）

（3）价值较低的数据，对事务性要求不高

对于这样的数据，我们更适合使用MongoDB来实现数据的存储。



行为    

**什么时候选择MongoDB** 

在架构选型上，除了上述的三个特点外，如果你还犹豫是否要选择它？可以考虑以下的一些问题：

应用不需要事务及复杂 join 支持

新应用，需求会变，数据模型无法确定，想快速迭代开发

应用需要2000-3000以上的读写QPS（更高也可以）

应用需要TB甚至 PB 级别数据存储

应用发展迅速，需要能快速水平扩展

应用要求存储的数据不丢失

应用需要99.999%高可用

应用需要大量的地理位置查询、文本查询

如果上述有1个符合，可以考虑 MongoDB，2个及以上的符合，选择 MongoDB 绝不会后悔。



思考：如果用MySQL呢？

答：相对MySQL，可以以更低的成本解决问题（包括学习、开发、运维等成本）



### 1.2 MongoDB简介

MongoDB是一个开源、高性能、无模式的文档型数据库，当初的设计就是用于简化开发和方便扩展，是NoSQL数据库产品中的一种。是最像关系型数据库（MySQL）的非关系型数据库。

它支持的数据结构非常松散，是一种类似于 JSON 的 格式叫BSON，所以它既可以存储比较复杂的数据类型，又相当的灵活。

MongoDB中的记录是一个文档，它是一个由字段和值对（field:value）组成的数据结构。MongoDB文档类似于JSON对象，即一个文档认为就是一个对象。字段的数据类型是字符型，它的值除了使用基本的一些类型外，还可以包括其他文档、普通数组和文档数组。



### 1.3 体系结构

MySQL和MongoDB对比

![1563642605752](assets/1563642605752.png)

| SQL术语/概念 | MongoDB术语/概念 | 解释/说明                           |
| :----------- | :--------------- | :---------------------------------- |
| database     | database         | 数据库                              |
| table        | collection       | 数据库表/集合                       |
| row          | document         | 数据记录行/文档                     |
| column       | field            | 数据字段/域                         |
| index        | index            | 索引                                |
| table joins  |                  | 表连接,MongoDB不支持                |
|              | 嵌入文档         | MongoDB通过嵌入式文档来替代多表连接 |
| primary key  | primary key      | 主键,MongoDB自动将_id字段设置为主键 |



### 1.4 数据模型

MongoDB的最小存储单位就是文档(document)对象。文档(document)对象对应于关系型数据库的行。数据在MongoDB中以BSON（Binary-JSON）文档的格式存储在磁盘上。

BSON（Binary Serialized Document Format）是一种类json的一种二进制形式的存储格式，简称Binary JSON。BSON和JSON一样，支持内嵌的文档对象和数组对象，但是BSON有JSON没有的一些数据类型，如Date和BinData类型。

BSON采用了类似于 C 语言结构体的名称、对表示方法，支持内嵌的文档对象和数组对象，具有轻量性、可遍历性、高效性的三个特点，可以有效描述非结构化数据和结构化数据。这种格式的优点是灵活性高，但它的缺点是空间利用率不是很理想。

Bson中，除了基本的JSON类型：string,integer,boolean,double,null,array和object，mongo还使用了特殊的数据类型。这些类型包括date,object id,binary data,regular expression 和code。每一个驱动都以特定语言的方式实现了这些类型，查看你的驱动的文档来获取详细信息。

BSON数据类型参考列表：

| **数据类型**  | **描述**                                                     | **举例**                                             |
| ------------- | ------------------------------------------------------------ | ---------------------------------------------------- |
| 对象id        | 对象id是文档的12字节的唯一   ID                              | {"X"   :ObjectId() }                                 |
| 字符串        | UTF-8字符串都可表示为字符串类型的数据                        | {"x"   : "foobar"}                                   |
| 布尔值        | 真或者假：true或者false                                      | {"x":true}+                                          |
| 数组          | 值的集合或者列表可以表示成数组                               | {"x"   ： ["a",   "b", "c"]}                         |
| 32位整数      | 类型不可用。JavaScript仅支持64位浮点数，所以32位整数会被自动转换。 | shell是不支持该类型的，shell中默认会转换成64位浮点数 |
| 64位整数      | 不支持这个类型。shell会使用一个特殊的内嵌文档来显示64位整数  | shell是不支持该类型的，shell中默认会转换成64位浮点数 |
| 64位浮点数    | shell中的数字就是这一种类型                                  | {"x"：3.14159，"y"：3}                               |
| null          | 表示空值或者未定义的对象                                     | {"x":null}                                           |
| undefined     | 文档中也可以使用未定义类型                                   | {"x":undefined}                                      |
| 符号          | shell不支持，shell会将数据库中的符号类型的数据自动转换成字符串 |                                                      |
| 正则表达式    | 文档中可以包含正则表达式，采用JavaScript的正则表达式语法     | {"x"   ： /foobar/i}                                 |
| 代码          | 文档中还可以包含JavaScript代码                               | {"x"   ： function() { /* …… */ }}                   |
| 二进制数据    | 二进制数据可以由任意字节的串组成，不过shell中无法使用        |                                                      |
| 最大值/最小值 | BSON包括一个特殊类型，表示可能的最大值。shell中没有这个类型。 |                                                      |

提示：

shell默认使用64位浮点型数值。{“x”：3.14}或{“x”：3}。对于整型值，可以使用NumberInt（4字节符号整数）或NumberLong（8字节符号整数），{“x”:NumberInt(“3”)}{“x”:NumberLong(“3”)}



### 1.5 MongoDB的特点

MongoDB主要有如下特点：

（1）**高性能**：

MongoDB提供高性能的数据持久性。特别是,

对嵌入式数据模型的支持减少了数据库系统上的I/O活动。

索引支持更快的查询，并且可以包含来自嵌入式文档和数组的键。（文本索引解决搜索的需求、TTL索引解决历史数据自动过期的需求、地理位置索引可用于构建各种 O2O 应用）

mmapv1、wiredtiger（默认）、mongorocks（rocksdb）、in-memory 等多引擎支持满足各种场景需求。

Gridfs解决文件存储的需求。

（2）**高可用性：**

MongoDB的复制工具称为副本集（replica set），它可提供自动故障转移和数据冗余。

（3）**高扩展性：**

MongoDB提供了水平可扩展性作为其核心功能的一部分。

分片将数据分布在一组集群的机器上。（海量数据存储，服务能力水平扩展）

从3.4开始，MongoDB支持基于片键创建数据区域。在一个平衡的集群中，MongoDB将一个区域所覆盖的读写只定向到该区域内的那些片。

（4）**丰富的查询支持：**

MongoDB支持丰富的查询语言，支持读和写操作(CRUD)，比如数据聚合、文本搜索和地理空间查询等。

（5）其他特点：如无模式（动态模式）、灵活的文档模型、

## 2 docker安装mongoDB

2.1 拉取镜像

```shell
docker pull mongo:4.2.5
```

2.2 创建容器

```shell
docker run --name mongo -p 27017:27017 -v ~/data/mongodata:/data -d mongo:4.2.5
```

![1587308221361](assets/1587308221361.png)

## 3 连接mongodb

studio3t是mongodb优秀的客户端工具。官方地址在https://studio3t.com/

或者使用：https://www.mongodb.com/try/download/compass

下载studio3t

![1587308417511](assets/1587308417511.png)

同学不用下载，可以使用资料文件夹中提供好的软件安装即可。

安装并启动

![1587308580032](assets/1587308580032.png)

创建连接

![1587308626866](assets/1587308626866.png)

![1587308663776](assets/1587308663776.png)

![1587308692439](assets/1587308692439.png)

连接成功：

![1587308807056](assets/1587308807056.png)

## 4 创建database和集合

### 4.1 创建database

使用studio3t创建database

右键-> Add Database

![1587308929789](assets/1587308929789.png)

输入名称

![1587308966514](assets/1587308966514.png)

创建成功：

![1587309010570](assets/1587309010570.png)

### 4.2 创建集合

右键leadnews-comment -> Add Collection

![1587309090213](assets/1587309090213.png)

输入名称

![1587309144060](assets/1587309144060.png)

创建成功

![1587309186683](assets/1587309186683.png)

## 5 springboot基础mongdb

### 5.1 搭建项目

#### 5.1.1 创建项目

创建项目：**mongodb-test**

pom文件：

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb</artifactId>
    </dependency>
</dependencies>
```

applicaton.yml

```yaml
server:
  port: 9001
spring:
  application:
    name: mongo-demo
  data:
    mongodb:
      host: 192.168.200.130
      database: leadnews-comment
```

引导类：

```java
package com.itheima.mongo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class MongoAppliction {
    public static void main(String[] args) {
        SpringApplication.run(MongoAppliction.class,args);
    }
}
```



#### 5.2.2 集成mongodb

### 5.2 mongodb的crud

#### 5.2.1 实体类准备

实体类准备，添加注解`@Document`与mongodb中的集合对应上

```java
package com.itheima.mongo.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
@Data
@Document(collection = "ap_comment")
public class ApComment {
    @Id
    private String id;
    private Integer authorId;
    private String authorName;
    private Integer entryId;
    private Integer channelId;
    private Boolean type;
    private String content;
    private String image;
    private Integer likes;
    private Integer reply;
    private Byte flag;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String address;
    private Integer ord;
    private Date createdTime;
    private Date updatedTime;

}
```

#### 5.2.2 基本增删改查

创建测试类：

```java
package com.itheima.mongo;

import com.itheima.mongo.pojo.ApComment;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Version: V1.0
 */
@SpringBootTest
class ApCommentTest {
  @Autowired
  MongoTemplate mongoTemplate;
  @Test
  public void testAdd() throws Exception{
    for (int i = 0; i < 10; i++) {
      ApComment apComment = new ApComment();
      //        apComment.setId("2222");
      apComment.setAddress("上海");
      apComment.setContent("上海是一个好地方222"+i);
      apComment.setLikes(2+i);
      // 60c2fe86c5a9e6651633a54a  ObjectId
      //        ApComment comment = mongoTemplate.insert(apComment);
      ApComment comment = mongoTemplate.save(apComment);
      System.out.println(comment);
    }
  }
  @Test
  public void testUpdate() throws Exception {
    Query query = Query.query(Criteria.where("address").is("上海"));
    Update update = new Update();
    update.set("address", "北京").set("createdTime", new Date());   // set修改器：只会对你指定列修改
    //        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, ApComment.class);
    //        System.out.println(updateResult);
    UpdateResult updateResult = mongoTemplate.updateMulti(query, update, ApComment.class);
    System.out.println(updateResult);
  }
  @Test
  public void testDelete() throws Exception {
    Query query = Query.query(Criteria.where("_id").is("60c2ffe79722b513119702b3"));
    DeleteResult deleteResult = mongoTemplate.remove(query, ApComment.class);
    System.out.println(deleteResult);
  }
  // 查询一个
  @Test
  public void testFindOne() throws Exception {
    //        ApComment comment = mongoTemplate.findById("60c2fe86c5a9e6651633a54a", ApComment.class);
    Query query = Query.query(Criteria.where("address").is("北京"));
    ApComment comment = mongoTemplate.findOne(query, ApComment.class);
    System.out.println(comment);
  }
  // 查询所有
  @Test
  public void testFindAll() throws Exception {
    List<ApComment> commentList = mongoTemplate.findAll(ApComment.class);
    for (ApComment apComment : commentList) {
      System.out.println(apComment);
    }
  }
  // 按照条件查询
  @Test
  public void testFindByQuery() throws Exception{
    Query query = Query.query(Criteria
                              .where("address").is("北京")
                              .and("_id").in("60c301de64ffe574631f0c44","60c301de64ffe574631f0c47")
                             );

    List<ApComment> apComments = mongoTemplate.find(query, ApComment.class);
    System.out.println(apComments);
  }
  // 分页查询
  @Test
  public void testFindPage() throws Exception {

    Query query = Query.query(Criteria
                              .where("address").is("北京")
                             );
    // 分页
    int page = 0;
    int size = 5;
    //        int limit = (page - 1) * size;
    //        query.limit(5);  // size

    Pageable pageable = PageRequest.of(1, 5);  // page=0 第一页
    query.with(pageable); // 分页

    Sort sort = Sort.by(Sort.Direction.DESC,"likes");
    query.with(sort);// 排序

    List<ApComment> commentList = mongoTemplate.find(query, ApComment.class);
    System.out.println(commentList);
  }

}
```

可以在studo3t中查看数据

![1587310274768](assets/1587310274768.png)



