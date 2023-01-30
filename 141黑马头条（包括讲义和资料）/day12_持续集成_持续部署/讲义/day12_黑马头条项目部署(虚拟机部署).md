# 第十四章 项目部署_持续集成

**学习目标**

- 能够理解什么是持续集成
- 能够完成jenkins环境的搭建
- 能够完成jenkins插件的安装及配置
- 能够理解黑马头条的部署架构
- 能够完成黑马头条的项目部署

## 1 单架构部署方案

### 1.1 部署流程

**传统方案**

![1613360499457](assets/1613360499457.png)

**基于docker**

![1613360577411](assets/1613360577411.png)

## 2 持续集成&持续部署方案

随着软件开发复杂度的不断提高，团队开发成员间如何更好地协同工作以确保软件
开发的质量已经慢慢成为开发过程中不可回避的问题。互联网软件的开发和发布，已经形成了一套标准流程。

如: 在互联网企业中，每时每刻都有需求的变更，bug的修复， 为了将改动及时更新到生产服务器上，下面的图片我们需要每天执行N多次，开发人员完整代码自测后提交到git，然后需要将git中最新的代码生成镜像并部署到测试服务器，如果测试通过了还需要将最新代码部署到生产服务器。如果采用手动方式操作，那将会浪费大量的时间浪费在运维部署方面。

![1605489947242](assets/1605489947242.png)

现在的互联网企业，基本都会采用以下方案解决:  

**持续集成（Continuous integration，简称 CI）。**

**持续部署（continuous deployment, 简称 CD）**



### 2.1 持续集成

持续集成 （Continuous integration，简称 CI） 指的是，频繁地（一天多次）将代码集成到主干。

它的好处主要有两个。

> 1、快速发现错误。每完成一点更新，就集成到主干，可以快速发现错误，定位错误也比较容易。

> 2、防止分支大幅偏离主干。如果不是经常集成，主干又在不断更新，会导致以后集成的难度变大，甚至难以集成。

持续集成的目的，就是让产品可以快速迭代，同时还能保持高质量。它的核心措施是，代码集成到主干之前，必须通过自动化测试。只要有一个测试用例失败，就不能集成。

Martin Fowler 说过，”持续集成并不能消除 Bug，而是让它们非常容易发现和改正。”

与持续集成相关的，还有两个概念，分别是持续交付和持续部署。

### 2.2 持续部署

持续部署（continuous deployment）是持续交付的下一步，指的是代码通过评审以后，**自动部署**到生产环境。

持续部署的目标是，代码在任何时刻都是可部署的，可以进入生产阶段。

持续部署的前提是能自动化完成测试、构建、部署等步骤。

### 2.3 流程说明

为了保证团队开发人员提交代码的质量，减轻了软件发布时的压力；
持续集成中的任何一个环节都是自动完成的，无需太多的人工干预，有利于减少重复
过程以节省时间、费用和工作量；接下来我们会演示一套基本的自动化持续集成和持续部署方案，来帮助大家理解互联网企业的软件部署方案。

计划如下:

![1605837774557](assets/1605837774557.png)

```
1. 开发人员将代码提交到 git 指定分支   如: dev

2. git仓库触发push事件，发送webhooks通知到持续集成软件

3. 持续集成软件触发构建任务，对dev分支的代码进行构建、编译、单元测试

4. 如果构建失败，发送邮件提醒代码提交人员或管理员

5. 如果构建成功，最新代码将会被构建Docker镜像并上传到注册中心

6. 构建成功触发webhooks通知容器编排软件，进行服务升级

7. 容器编排软件，触发对应的服务升级任务， 将创建对应服务的新容器替换之前的容器

8. 完成最新代码的自动构建与自动部署，全程无工作人员干预
```



### 2.4 jenkins安装部署

#### 2.4.1 Jenkins介绍

![1603175880959](assets/1603175880959.png)

Jenkins  是一款流行的开源持续集成（Continuous Integration）工具，广泛用于项目开发，具有自动化构建、测试和部署等功能。官网：  http://jenkins-ci.org/。

Jenkins的特征：

- 开源的 Java语言开发持续集成工具，支持持续集成，持续部署。
- 易于安装部署配置：可通过 yum安装,或下载war包以及通过docker容器等快速实现安装部署，可方便web界面配置管理。
- 消息通知及测试报告：集成 RSS/E-mail通过RSS发布构建结果或当构建完成时通过e-mail通知，生成JUnit/TestNG测试报告。
- 分布式构建：支持 Jenkins能够让多台计算机一起构建/测试。
- 文件识别： Jenkins能够跟踪哪次构建生成哪些jar，哪次构建使用哪个版本的jar等。
- 丰富的插件支持：支持扩展插件，你可以开发适合自己团队使用的工具，如 git，svn，maven，docker等。



Jenkins安装和持续集成环境配置

![1603176000242](assets/1603176000242.png)

1 ）首先，开发人员每天进行代码提交，提交到Git仓库

2）然后，Jenkins作为持续集成工具，使用Git工具到Git仓库拉取代码到集成服务器，再配合JDK，Maven等软件完成代码编译，代码测试与审查，测试，打包等工作，在这个过程中每一步出错，都重新再执行一次整个流程。

3）最后，Jenkins把生成的jar或war包分发到测试服务器或者生产服务器，测试人员或用户就可以访问应用。

#### 2.4.2 Jenkins环境搭建

可以导入资料中的镜像：

>服务器 IP：192.168.200.100 用户名：root   密码：itcast
>
>jenkins  用户名：itcast   密码：itcast

1. 采用YUM方式安装

   加入jenkins安装源：

   ```sh
   sudo wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo --no-check-certificate
   
   sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key
   ```

   执行yum命令安装：

   ```sh
   yum -y install jenkins
   ```

2. 采用RPM安装包方式

   [Jenkins安装包下载地址](https://pkg.jenkins.io/redhat-stable/)

   ```sh
   wget https://pkg.jenkins.io/redhat-stable/jenkins-2.222.3-1.1.noarch.rpm
   ```

   执行安装：

   ```sh
   rpm -ivh jenkins-2.249-1.1.noarch.rpm
   ```

3. 配置：

   修改配置文件：

   ```sh
   vi /etc/sysconfig/jenkins
   ```

   修改内容：

   ```sh
   # 修改为对应的目标用户， 这里使用的是root
   $JENKINS_USER="root"
   # 服务监听端口
   JENKINS_PORT="16060"
   ```

   目录权限：

   ```sh
   chown -R root:root /var/lib/jenkins
   chown -R root:root /var/cache/jenkins
   chown -R root:root /var/log/jenkins
   ```

   重启：

   ```sh
   systemctl restart jenkins
   ```

   如果启动失败， 出现错误信息：

   ```sh
   Starting Jenkins bash: /usr/bin/java: No such file or directory
   ```

   创建JAVA环境的软链接：

   ```sh
   ln -s /usr/local/jdk/bin/java /usr/bin/java
   ```

   **注意:** 如果阿里云服务器中未安装JDK 可以直接按下面方式安装

   https://www.cnblogs.com/zwx655/p/13947562.html

   ```
   yum install java-1.8.0-openjdk* -y
   ```

   
   
   管理后台初始化设置

   http://192.168.200.100:16060/

   需要输入管理密码， 在以下位置查看：

   ```sh
   cat /var/lib/jenkins/secrets/initialAdminPassword
   ```

   ![1569564399216](assets/1569564399216.png)
   
   按默认设置，把建议的插件都安装上
   
   ![1569564606846](assets/1569564606846.png)
   
   这一步等待时间较长， 安装完成之后， 创建管理员用户：
   
   ![1569564966999](assets/1569564966999.png)

配置访问地址：

![1569564989527](assets/1569564989527.png)



配置完成之后， 会进行重启， 之后可以看到管理后台：

![1569565238541](assets/1569565238541.png)

#### 2.4.3  Jenkins插件安装

在实现持续集成之前， 需要确保以下插件安装成功。

- Maven Integration plugin： Maven 集成管理插件。
- Docker plugin： Docker集成插件。
- GitLab Plugin： GitLab集成插件。
- git Plugin: git集成插件
- Publish Over SSH：远程文件发布插件。
- SSH: 远程脚本执行插件。

安装方法：

1. 进入【系统管理】-【插件管理】

2. 点击标签页的【可选插件】

   在过滤框中搜索插件名称

   ![1569742624798](assets/1569742624798.png)

3. 勾选插件， 点击直接安装即可。

>注意，如果没有安装按钮，需要更改配置
>
>在安装插件的高级配置中，修改升级站点的连接为：http://updates.jenkins.io/update-center.json   保存
>
>![1603521604783](assets/1603521604783.png)
>
>![1603521622211](assets/1603521622211.png)




#### 2.4.3  Maven安装配置

1. 下载安装包

   下载地址： https://maven.apache.org/download.cgi

2. 解压安装包

   ```sh
   cd /usr/local
   unzip -o apache-maven-3.3.9.zip
   
   # 不支持unzip
   yum install -y unzip
   ```

3. 配置

   环境变量配置

   ```sh
   vi /etc/profile
   ```

   增加：

   ```sh
   export MAVEN_HOME=/usr/local/apache-maven-3.3.9
   export PATH=$PATH:$MAVEN_HOME/bin
   ```

   如果权限不够，则需要增加当前目录的权限

   ```shell
   chmod 777 /usr/local/apache-maven-3.3.9/bin/mvn
   ```

   

   修改镜像仓库配置：

   ```sh
   vi /usr/local/apache-maven-3.3.9/conf/settings.xml
   
   # 让配置生效
   source /etc/profile
   
   ```

   需要把本机的仓库打包上传到服务器上（不上传会自动下载）
   
   然后指定上传后的仓库配置
   
   ![1603521851632](assets/1603521851632.png)

#### 2.4.4  Docker安装配置(已安装)

1. 更新软件包版本

   ```sh
   yum -y update
   ```

2. 卸载旧版本

   ```sh
   yum -y remove docker  docker-common docker-selinux docker-engine
   ```

3. 安装软件依赖包

   ```sh
   yum install -y yum-utils device-mapper-persistent-data lvm2
   ```

4. 设置yum源为阿里云

   ```sh
   sudo yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
   ```

5. 安装后查看docker版本

   ```sh
   docker -v
   ```

6. 启动

   设置开机启动：

   ```sh
   systemctl enable docker
   ```

   启动docker

   ```sh
   systemctl start docker
   ```


#### 2.4.5  Docker Registry私有仓库安装配置

对于持续集成环境的配置，Jenkins会发布大量的微服务， 要与多台机器进行交互， 可以采用docker镜像的保存与导出功能结合SSH实现， 但这样交互繁琐，稳定性差， 而且不便管理， 这里我们通过搭建Docker的私有仓库来实现， 这个有点类似GIT仓库， 集中统一管理资源， 由客户端拉取或更新。

1. 下载最新Registry镜像

   ```sh
   docker pull registry:latest
   ```

2. 启动Registry镜像服务

   ```sh
   docker run -d -p 5000:5000 --name registry -v /usr/local/docker/registry:/var/lib/registry registry:latest
   ```

   映射5000端口； -v是将Registry内的镜像数据卷与本地文件关联， 便于管理和维护Registry内的数据。

   > 删除/usr/local/docker/registry/docker/registry/v2/repositories 下的数据

3. 查看仓库资源

   访问地址：http://192.168.200.100:5000/v2/_catalog

   启动正常， 可以看到返回：

   ```json
   {"repositories":[]}
   ```

   目前并没有上传镜像， 显示空数据。

   如果上传成功， 可以看到数据:
   ![1603522230134](assets/1603522230134.png)

4. 配置Docker客户端

   正常生产环境中使用， 要配置HTTPS服务， 确保安全，内部开发或测试集成的局域网环境，可以采用简便的方式， 不做安全控制。

   先确保持续集成环境的机器已安装好Docker客户端， 然后做以下修改：

   ```sh
   vi /lib/systemd/system/docker.service
   ```

   修改内容：

   ```sh
   ExecStart=/usr/bin/dockerd --insecure-registry 192.168.200.100:5000
   ```

   指向安装Registry的服务IP与端口。

   重启生效：

   ```sh
   systemctl daemon-reolad
   systemctl restart docker.service
   ```

### 2.5 持续集成生产实践配置

1. 进入【系统管理】--> 【全局工具配置】

   ![1603200359461](assets/1603200359461.png)

2. MAVEN配置全局设置

   ![1603200435502](assets/1603200435502.png)

3. 指定JDK配置

   ![1603200401796](assets/1603200401796.png)



4. 指定MAVEN 目录

   ![1603200472287](assets/1603200472287.png)

5. 指定DOCKER目录

   ![1603200520246](assets/1603200520246.png)

   如果不清楚docker的安装的目录，可以使用`whereis docker` 命令查看docker的安装的目录

   如果 阿里云服务器中的git配置报红:  `yum -y install git`

6. 设置远程应用服务主机

   添加凭证：

   ![1603735746602](assets/1603735746602.png)

   新增凭证，输入用户名和密码保存即可

   ![1603735794051](assets/1603735794051.png)

   

   进入【系统管理】-【系统设置】

   ![1603200611899](assets/1603200611899.png)

   输入主机名称和登陆信息， 点击【check connections】验证， 如果成功， 会显示“Successfull connection”。

   ![1603735668415](assets/1603735668415.png)



## 3 黑马头条项目部署演示

黑马头条涉及前端后端服务众多，设备性能有限 所以我们简化下部署步骤， 基于docker + docker-compose方式快速部署，先来了解下黑马头条的部署架构

![1613375860330](assets/1613375860330.png)

1. nginx作为接入层 所有请求全部通过nginx进入 (部署在100服务 端口80)
2. 前端工程app、admin、wemedia全部部署在nginx中
3. nginx通过反向代理将对微服务的请求，代理到网关
4. 网关根据路由规则，将请求转发到下面的微服务
5. 所有微服务都会注册到nacos注册中心
6. 所有微服务都会将配置存储到nacos中进行统一配置
7. 网关服务及所有微服务部署在 100  服务器   jenkins    微服务
8. 所有依赖的软件部署在    131   服务器

### 3.1 相关软件部署

MAC下或者Windows下的Docker自带Compose功能，无需安装。

Linux下需要通过命令安装：

```sh
# 安装
curl -L https://github.com/docker/compose/releases/download/1.24.1/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose


# 上传资料中的 docker-compose 文件到 /usr/local/bin/

# 修改权限
chmod +x /usr/local/bin/docker-compose

ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
```



**黑马头条相关软件 一键脚本**

```yaml
# 涉及软件 容器启动脚本
# 将 当前文件夹内容拷贝到有docker的虚拟机
# 通过docker命令即可启动所有软件
version: '3'
services:
  mysql:
    image: mysql:5.7
    ports:
      - "3306:3306"
    volumes:
      - "/root/mysql/conf:/etc/mysql/conf.d"
      - "/root/mysql/logs:/logs"
      - "/root/mysql/data:/var/lib/mysql"
    environment:
      - MYSQL_ROOT_PASSWORD=root
    restart: always
  nacos:
    image: nacos/nacos-server:1.3.2
    ports:
      - "8848:8848"
    restart: always
    environment:
      - MODE=standalone
      - JVM_XMS=256m
      - JVM_XMX=256m
      - JVM_XMN=128m
      - SPRING_DATASOURCE_PLATFORM=mysql
      - MYSQL_SERVICE_HOST=mysql
      - MYSQL_SERVICE_PORT=3306
      - MYSQL_SERVICE_USER=root
      - MYSQL_SERVICE_PASSWORD=root
      - MYSQL_SERVICE_DB_NAME=nacos_config
      - NACOS_SERVER_IP=47.100.130.118
    depends_on:
      - mysql
  seata:
    image: seataio/seata-server:1.3.0
    ports:
      - "8091:8091"
    environment:
      - "SEATA_IP=47.100.130.118"
    restart: always
  zookeeper:
    image: zookeeper:3.4.14
    restart: always
    expose:
      - 2181
  kafka:
    image: wurstmeister/kafka:2.12-2.3.1
    environment:
      KAFKA_ADVERTISED_HOST_NAME: 47.100.130.118
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://47.100.130.118:9092
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092
      KAFKA_HEAP_OPTS: "-Xmx256M -Xms256M"
    ports:
      - "9092:9092"
    depends_on:
      - zookeeper
  xxljob:
    image: xuxueli/xxl-job-admin:2.2.0
    volumes:
      - "/tmp:/data/applogs"
    environment:
      PARAMS: "--spring.datasource.url=jdbc:mysql://47.100.130.118:3306/xxl_job?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai --spring.datasource.username=root --spring.datasource.password=root"
    ports:
      - "8888:8080"
    depends_on:
      - mysql
  reids:
    image: redis
    ports:
      - "6379:6379"
  mongo:
    image: mongo:4.2.5
    ports:
      - "27017:27017"
  elasticsearch:
    image: elasticsearch:7.4.2
    ports:
      - "9200:9200"
      - "9300:9300"
    environment:
      - "discovery.type=single-node"
      - "ES_JAVA_OPTS=-Xms256m -Xmx256m"
    volumes:
      - "/usr/share/elasticsearch/plugins:/usr/share/elasticsearch/plugins"
  kibana:
    image: kibana:7.4.2
    links:
      - elasticsearch
    environment:
      - "ELASTICSEARCH_URL=http://elasticsearch:9200"
    ports:
      - "5601:5601"
    depends_on:
      - elasticsearch
```



**使用步骤**

1. 运行所有容器：

```
# 运行
docker-compose up -d

# 停止
docker-compose stop

# 停止并删除容器
docker-compose down

# 查看日志
docker-compose logs -f [service...]

# 查看命令
docker-compose --help
```

2. 导入sql语句

```
导入微服务相关数据库

导入xxljob使用数据库

导入nacos配置中心数据库

导入seata分布式事务数据库
```

3. 安装es 中文ik分词器

```
把资料中的 elasticsearch-analysis-ik-7.4.2.zip 上传到服务器上,放到对应目录（plugins）解压

#切换目录
cd /usr/share/elasticsearch/plugins
#新建目录
mkdir analysis-ik
cd analysis-ik
#root根目录中拷贝文件
mv elasticsearch-analysis-ik-7.4.2.zip /usr/share/elasticsearch/plugins/analysis-ik
#解压文件
cd /usr/share/elasticsearch/plugins/analysis-ik
unzip elasticsearch-analysis-ik-7.4.2.zip
```

4. 创建es索引库

```
PUT app_info_article
{
    "mappings":{
        "properties":{
            "id":{
                "type":"long"
            },
            "publishTime":{
                "type":"date"
            },
            "layout":{
                "type":"integer"
            },
            "images":{
                "type":"keyword"
            },
            "authorId": {
          		"type": "long"
       		},
          "title":{
            "type":"text",
            "analyzer":"ik_smart"
          }
        }
    }
}
```

### 3.2 nacos统一配置管理

在需要运行的微服务中

**注意:  下面两种配置没有整合 需要单独修改**

​		      seata 起步依赖中    file.conf 中的seata服务端连接地址需要修改

​              comment微服务中  redission的配置需要修改

加入nacos配置依赖

```xml
 		<dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        </dependency>
```

注释掉application.yml配置文件,创建bootstrap.yml配置文件

```yml
spring:
  application:
    name: leadnews-admin  # 微服务名称
  profiles:
    active: dev  # 配置环境
  cloud:
    nacos:
      discovery:  
        server-addr: 192.168.200.129:8848 # 注册中心
      config:
        file-extension: yml # 配置文件后缀
        server-addr: 192.168.200.129:8848 # 配置中心
```

![1613379539546](assets/1613379539546.png)

将application.yml中的配置文件内容复制到nacos中

![1613379700439](assets/1613379700439.png)



配置文件是可导入导出的，导入资料中的nacos_config配置压缩包

然后根据自己的配置情况进行修改

![1613379876496](assets/1613379876496.png)

### 3.3 微服务持续部署

每个微服务使用的dockerfile的方式进行构建镜像后创建容器，需要在每个微服务中添加docker相关的配置

（1）修改**每个微服务**的pom文件，添加dockerfile的插件

```xml
	<properties>
        <docker.image>docker_storage</docker.image>
    </properties>
    <build>
        <finalName>heima-leadnews-wemedia</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.7.0</version>
                <configuration>
                    <source>${java.version}</source>
                    <target>${java.version}</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>com.spotify</groupId>
                <artifactId>dockerfile-maven-plugin</artifactId>
                <version>1.3.6</version>
                <configuration>
                    <repository>${docker.image}/${project.build.finalName}</repository>
                    <buildArgs>
                        <JAR_FILE>target/${project.build.finalName}.jar</JAR_FILE>
                    </buildArgs>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

（2）在每个微服务的根目录下创建Dockerfile文件，如下：

```dockerfile
# 设置JAVA版本
FROM java:8
# 指定存储卷, 任何向/tmp写入的信息都不会记录到容器存储层
VOLUME /tmp
# 拷贝运行JAR包
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
# 设置JVM运行参数， 这里限定下内存大小，减少开销
ENV JAVA_OPTS="\
-server \
-Xms256m \
-Xmx512m \
-XX:MetaspaceSize=256m \
-XX:MaxMetaspaceSize=512m"
# 入口点， 执行JAVA运行命令
ENTRYPOINT java ${JAVA_OPTS}  -jar /app.jar
```

![1603556179776](assets/1603556179776.png)

#### 3.3.1 基础依赖打包配置

在微服务运行之前需要在本地仓库中先去install所依赖的jar包，所以第一步应该是从git中拉取代码，并且把基础的依赖部分安装到仓库中

（1）新创建一个item,起名为heima-leadnews

![1603202842506](assets/1603202842506.png)



![1603556912178](assets/1603556912178.png)

（2）配置当前heima-leadnews

- 描述项目

![1603556984260](assets/1603556984260.png)

- 源码管理：

  选中git，输入git的仓库地址（前提条件，需要把代码上传到gitee仓库中），最后输入gitee的用户名和密码

  如果没有配置Credentials，可以选择添加，然后输入用户名密码即可 **(公开仓库无需密码)**

![1603557100765](assets/1603557100765.png)

- 其中构**建触发器**与**构建环境**暂不设置

- 设置**构建**配置

  选择`Invoke top-level Maven targets`

![1603557353285](assets/1603557353285.png)

​	maven版本：就是之前在jenkins中配置的maven

​	目标：输入maven的命令  `clean install -Dmaven.test.skip=true`  跳过测试安装

![1603557446935](assets/1603557446935.png)

(3)启动项目

创建完成以后可以在主页上看到这个item

![1603557661626](assets/1603557661626.png)

启动项目：点击刚才创建的项目，然后Build Now

![1603557703328](assets/1603557703328.png)

在左侧可以查看构建的进度：

![1603557774990](assets/1603557774990.png)

点进去以后，可以查看构建的日志信息

构建的过程中，会不断的输入日志信息，如果报错也会提示错误信息

![1603557824124](assets/1603557824124.png)

jenkins会先从git仓库中拉取代码，然后执行maven的install命令，把代码安装到本地仓库中

最终如果是success则为构建成功

![1603557903228](assets/1603557903228.png)

#### 3.3.2 微服务打包配置

（1）新建item，以**heima-leadnews-admin**微服务为例

![1603558664549](assets/1603558664549.png)

(2)配置

- 概述

![1603558714167](assets/1603558714167.png)

- 源码管理

![1603558748960](assets/1603558748960.png)

- 构建

配置maven

![1603558861469](assets/1603558861469.png)

**执行maven命令：**

```
clean install -Dmaven.test.skip=true -P dev dockerfile:build -f heima-leadnews-services/admin-service/pom.xml
```

**注意目录接口， maven命令要找到pom.xml的位置**

>-Dmaven.test.skip=true   跳过测试
>
>-P prod  指定环境为生成环境
>
>dockerfile:build  启动dockerfile插件构建容器
>
>-f heima-leadnews-admin/pom.xml  指定需要构建的文件（必须是pom）

![image-20210427115048199](assets/image-20210427115048199.png)

![1603558973180](assets/1603558973180.png)

执行shell命令

![1603558910209](assets/1603558910209.png)

![1603559164996](assets/1603559164996.png)

```shell
if [ -n  "$(docker ps -a -f  name=heima-$JOB_NAME  --format '{{.ID}}' )" ]
 then
 #删除之前的容器
 docker rm -f $(docker ps -a -f  name=heima-$JOB_NAME  --format '{{.ID}}' )
fi
 # 清理镜像
docker image prune -f 
 # 启动docker服务
docker run -d --net=host  --name heima-$JOB_NAME docker_storage/heima-$JOB_NAME
```



这里不是只单纯的启动服务， 我们要考虑每次构建， 都会产生镜像， 所以要先做检查清理， 然后再启动服务。

Docker有五种网络连接模式， 因为我们不是所有服务都采用docker构建， 中间件服务部署在宿主机上面， 这里我们采用host模式， 这样docker容器和主机服务之间就是互通的。

- bridge模式

  使用命令： --net=bridge， 这是dokcer网络的默认设置，为容器创建独立的网络命名空间，容器具有独立的网卡等所有单独的网络栈，这是默认模式。

- host模式

  使用命令： --net=host，直接使用容器宿主机的网络命名空间， 即没有独立的网络环境。它使用宿主机的ip和端口。

- none模式

  命令： --net=none,  为容器创建独立网络命名空间, 这个模式下，dokcer不为容器进行任何网络配置。需要我们自己为容器添加网卡，配置IP。

- container模式

  命令： --net=container:NAME_or_ID， 与host模式类似， 这个模式就是指定一个已有的容器，共享该容器的IP和端口。

- 自定义模式

  docker 1.9版本以后新增的特性，允许容器使用第三方的网络实现或者创建单独的bridge网络，提供网络隔离能力。



**到此就配置完毕了，保存即可**

（3）启动该项目 Build Now

- 首先从git中拉取代码
- 编译打包项目
- 构建镜像
- 创建容器
- 删除多余的镜像

可以从服务器中查看镜像

![1603559728626](assets/1603559728626.png)

容器也已创建完毕

![1603559790306](assets/1603559790306.png)

可以使用postman测试测试该服务接口

#### 3.3.2 构建其他微服务

可以参考admin微服务创建其他微服务，每个项目可能会有不同的maven构建命令，请按照实际需求配置

- heima-leadnews-admin-gateway微服务的配置：

```
maven命令：

clean install -Dmaven.test.skip=true dockerfile:build -f  heima-leadnews-gateways/admin-gateway/pom.xml
```



>![1603561212541](assets/1603561212541.png)



heima-leadnews-user微服务的配置：

```
maven命令：

clean install -Dmaven.test.skip=true dockerfile:build -f heima-leadnews-services/user-service/pom.xml
```

>![1603561293105](assets/1603561293105.png)

所有项目构建完成以后，在本地启动admin前端工程，修改configs中的网关地址为：`192.168.200.100`,进行效果测试

**同样方式配置其它微服务**

### 3.4 接入层及前端部署

#### 3.4.1 接入层nginx搭建

官方网站下载 nginx：http://nginx.org/，也可以使用资料中的安装包，版本为：nginx-1.18.0

**安装依赖**

- 需要安装 gcc 的环境

```shell
yum install gcc-c++
```

- 第三方的开发包。

  - PCRE(Perl Compatible Regular Expressions)是一个 Perl 库，包括 perl 兼容的正则表达式库。nginx 的 http 模块使用 pcre 来解析正则表达式，所以需要在 linux 上安装 pcre 库。

    ```shell
    yum install -y pcre pcre-devel
    ```

    注：pcre-devel 是使用 pcre 开发的一个二次开发库。nginx 也需要此库。

  - zlib 库提供了很多种压缩和解压缩的方式，nginx 使用 zlib 对 http 包的内容进行 gzip，所以需要在 linux 上安装 zlib 库。

    ```shell
    yum install -y zlib zlib-devel
    ```

  - OpenSSL 是一个强大的安全套接字层密码库，囊括主要的密码算法、常用的密钥和证书封装管理功能及 SSL 协议，并提供丰富的应用程序供测试或其它目的使用。nginx
    不仅支持 http 协议，还支持 https（即在 ssl 协议上传输 http），所以需要在 linux安装 openssl 库。

    ```shell
    yum install -y openssl openssl-devel
    ```

**Nginx安装**

第一步：把 nginx 的源码包nginx-1.18.0.tar.gz上传到 linux 系统

第二步：解压缩

```shell
tar -zxvf nginx-1.18.0.tar.gz
```

第三步：进入nginx-1.18.0目录   使用 configure 命令创建一 makeFile 文件。

```shell
./configure \
--prefix=/usr/local/nginx \
--pid-path=/var/run/nginx/nginx.pid \
--lock-path=/var/lock/nginx.lock \
--error-log-path=/var/log/nginx/error.log \
--http-log-path=/var/log/nginx/access.log \
--with-http_gzip_static_module \
--http-client-body-temp-path=/var/temp/nginx/client \
--http-proxy-temp-path=/var/temp/nginx/proxy \
--http-fastcgi-temp-path=/var/temp/nginx/fastcgi \
--http-uwsgi-temp-path=/var/temp/nginx/uwsgi \
--http-scgi-temp-path=/var/temp/nginx/scgi
```

执行后可以看到Makefile文件

第四步：编译

```shell
make
```

第五步：安装

```shell
make install
```

第六步：启动

注意：启动nginx 之前，上边将临时文件目录指定为/var/temp/nginx/client， 需要在/var  下创建此
目录

```shell
mkdir /var/temp/nginx/client -p
```

进入到Nginx目录下的sbin目录

```shell
cd /usr/local/nginx/sbin
```

输入命令启动Nginx

```shell
./nginx
```

启动后查看进程

```shell
ps aux|grep nginx
```

![1613376602041](assets/1613376602041.png)



#### 3.4.2 发布前端工程

前端在开发时，是基于node环境在本地开发，引用了非常多的基于node的js 在开发完毕后也许要发布，webpack依赖就是用于发布打包的，它会将很多依赖的js进行整合，最终打包成 html css js 这三种格式的文件，我们把发布后的静态文件拷贝到nginx管理的文件夹中，即可完成部署

```shell
# 创建目录  用于存放对应的前端静态资源
mkdir -p /root/workspace/admin 
mkdir -p /root/workspace/web
mkdir -p /root/workspace/wemedia
```

**admin前端工程发布**

在admin工程下，打开cmd  输入: `npm run build` 进行发布

发布后的静态文件，会存放到dist文件夹中

![1613377149886](assets/1613377149886.png)

![1613377225038](assets/1613377225038.png)

把dist文件夹上传到服务器上，拷贝到150虚拟机的/root/workspace/admin目录中

**wemedia前端工程发布**

在wemedia工程下，打开cmd  输入: `npm run build` 进行发布

发布后的静态文件，会存放到dist文件夹中

![1613377649182](assets/1613377649182.png)

把dist文件夹上传到服务器上，拷贝到150虚拟机的/root/workspace/wemedia目录中

**app前端工程发布**

前端工程比较特殊，因为使用了被称为三端合一的weex框架,也就是说它即可以发布android端，也可以发布ios端，也可以发布web端。命令会有区别

在app工程下，打开cmd  输入: `npm run clean:web && npm run build:prod:web` 进行发布web端

```
小贴士: 其它端需要安装对应软件才能发布，比如android需要有android studio
npm run pack:android   发布安卓
npm run pack:ios       发布ios
```

![1613377967699](assets/1613377967699.png)

把releases文件夹下的web文件夹上传到服务器上，拷贝到150虚拟机的/root/workspace/目录中

#### 3.4.3 nginx配置前端工程访问

对于不同的前端工程 ， 我们会通过不同的域名来访问, 先给三个前端工程准备3个访问域名

1. 使用type下载hosts插件
2. 配置3个域名:   
3. 47.100.130.118  admin.leadnews.com    运营端
4. 47.100.130.118  wemedia.leadnews.com    媒体端
5. 47.100.130.118  web.leadnews.com    app端



![1613378464091](assets/1613378464091.png)

![1613378379712](assets/1613378379712.png)

```
小贴士: 
如果想部署外网访问的项目，可以使用内网穿透 准备三个外网地址
全部映射到
47.100.130.118  的 80 端口

下面的nginx也使用对应的外网地址
```



打开linux的目录：/usr/local/nginx/conf

编辑nginx.conf文件，替换如下:

```nginx
user  root;
worker_processes  1;
events {
    worker_connections  1024;
}
http {
    include       mime.types;
    default_type  application/octet-stream;
    
    # 反向代理配置 代理admin gateway
    upstream  heima-admin-gateway{
        server 192.168.200.100:6001;  
    }
    # 反向代理配置 代理wemedia gateway
    upstream  heima-wemedia-gateway{
       server 192.168.200.100:6002;
    }
    # 反向代理配置 代理app gateway
    upstream  heima-app-gateway{
       server 192.168.200.100:5001;
    }
    
    server {
	listen 80;
	server_name localhost;
        location / {
            root /usr/local/nginx/html;
            index index.html ;
        }	
     }
     server {
        listen 80;
        server_name hmttapp.cn.utools.club;
        location / {
            root /root/workspace/web;
            index index.html ;
        }   
        location ~/app/(.*) {
            proxy_pass http://heima-app-gateway/$1;
            proxy_set_header HOST $host;
            proxy_pass_request_body on;
            proxy_pass_request_headers on;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }
     }
     server {
        listen 80;
        server_name hmttadmin.cn.utools.club;
        location / {
            root /root/workspace/admin/dist;
            index index.html ;
        }
        location ~/service_6001/(.*) {
            proxy_pass http://heima-admin-gateway/$1;
            proxy_set_header HOST $host;
            proxy_pass_request_body on;
            proxy_pass_request_headers on;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }          
     }  
     server {
        listen 80;
        server_name hmttwemedia.cn.utools.club;
        location / {
            root /root/workspace/wemedia/dist;
            index index.html ;
        }
        location ~/wemedia/MEDIA/(.*) {
            proxy_pass http://heima-wemedia-gateway/$1;
            proxy_set_header HOST $host;
            proxy_pass_request_body on;
            proxy_pass_request_headers on;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }                              
     } 
}
```

配置完毕后，重启nginx

命令: `/usr/local/nginx/sbin/nginx -s reload`



输入网址访问前端工程:

![1613378750447](assets/1613378750447.png)

![1613378843939](assets/1613378843939.png)

### 3.5 前后端联调测试

访问前端工程，测试各类功能，完成项目部署







### 3.6 自动通知jenkins触发任务

主流的git软件都提供了webhooks功能(web钩子), 通俗点说就是git在发生某些事件的时候可以通过POST请求调用我们指定的URL路径，那在这个案例中，我们可以在push事件上指定jenkins的任务通知路径。

#### 3.6.1 jenkins配置Gitee插件

**jenkins下载webhooks插件**

gitee插件介绍: https://gitee.com/help/articles/4193#article-header0

jenkins也支持通过url路径来启动任务，具体设置方法: 

jenkins的默认下载中仅下载了github的通知触发,我们需要先下载一个插件

(1) 下载gitee插件

系统管理-->插件管理-->可选插件-->搜索 `Gitee` 下载-->重启jenkins

![1606929689394](assets/1606929689394.png)

(2) gitee生成访问令牌

   首先，去下面网址生成gitee访问令牌

   https://gitee.com/profile/personal_access_tokens

![1606929726997](assets/1606929726997.png)

添加令牌描述，提交，弹出框输入密码

![1606929755320](assets/1606929755320.png)

复制令牌

![1606929779247](assets/1606929779247.png)

（3） jenkins中配置Gitee

系统管理 --> 系统配置 --> Gitee配置

1. 链接名: gitee
2. 域名: https://gitee.com
3. 令牌: Gitee Api 令牌   (需要点击添加按下图配置)
4. 配置好后测试连接
5. 测试成功后保存配置

![1606929817791](assets/1606929817791.png)

令牌配置: 

1. 类型选择Gitee API令牌
2. 私人令牌: 将码云中生成的令牌复制过来
3. 点击添加

![1606929845138](assets/1606929845138.png)

#### 3.6.2 修改jenkins构建任务

**修改配置接收webhooks通知**

任务详情中点击配置来修改任务

![1606929890564](assets/1606929890564.png)

点击构建触发器页签,勾选`Gitee webhook`

![1606929947046](assets/1606929947046.png)

生成Gitee Webhook密码

![1606929980749](assets/1606929980749.png)

保存好触发路径和webhook密码，到gitee中配置webhook通知

如: 

触发路径:  http://192.168.200.151:8888/gitee-project/dockerDemo

触发密码: a591baa17f90e094500e0a11b831af9c

#### 3.6.3 Gitee添加webhooks通知

**gitee仓库配置webhooks通知**

点击仓库页面的管理

![1606930088046](assets/1606930088046.png)

添加webhook

1. 点击webhooks菜单，然后点击添加
2. 配置jenkins通知地址
3. 填写密码
4. 点击添加

![1606930153212](assets/1606930153212.png)

但在点击添加时，提示失败 gitee中需要配置一个公有IP或域名，这里我们可以通过内网穿透来解决

![1606930181199](assets/1606930181199.png)

这个时候需要使用内网穿透来映射本地的ip和端口号

![1606930276099](assets/1606930276099.png)

在gitee中将上面的外网地址替换之前的ip和端口部分，再次添加

![1606930326225](assets/1606930326225.png)

#### 3.6.4 测试自动构建

添加完毕后测试一下:

提交leadnews-admin的代码测试是否自动触发了jenkins中的构建任务

![1606930472587](assets/1606930472587.png)







