01-昨日回顾_今日目标..wmv
02-快速回顾-Stream流的基本步骤.wmv
03-快速回顾-Stream流的创建.wmv
04-快速回顾-Stream流的中间步骤.wmv
05-快速回顾-Stream流的终止操作.wmv
06-自媒体文章_整体流程概要说明.wmv
07-自媒体文章_列表查询_需求分析.wmv
08-自媒体文章_文章列表查询_功能实现.wmv
09-自媒体文章_文章列表查询_测试.wmv
10-自媒体文章_频道列表_功能实现及测试.wmv
11-自媒体文章_发表文章_需求分析.wmv
12-自媒体文章_发表文章_检查参数封装文章实体.wmv
13-自媒体文章_发表文章_保存或修改文章.wmv

14-自媒体文章_发表文章_保存文章内容与素材关联关系.wmv
15-自媒体文章_发表文章_保存文章封面与素材关联关系.wmv
16-自媒体文章_发表文章_前后端联调测试.wmv
17-自媒体文章_文章查询_功能实现及测试.wmv
18-自媒体文章_文章删除_需求分析.wmv
19-自媒体文章_文章删除_功能实现.wmv
20-自媒体文章_文章删除_测试.wmv
21-自媒体文章_文章上下架_需求分析.wmv
22-自媒体文章_文章上下架_功能实现.wmv
23-自媒体文章_文章上下架_测试.wmv
24-今日总结.wmv

docker run -d \
-e PREFER_HOST_MODE=hostname \
-e MODE=standalone \
-e SPRING_DATASOURCE_PLATFORM=mysql \
-e MYSQL_SERVICE_HOST=111.229.11.42 \
-e MYSQL_SERVICE_PORT=3306 \
-e MYSQL_SERVICE_USER=root \
-e MYSQL_SERVICE_PASSWORD='3Q#ePydHxwgX4jmnGw$m*h*fiU$ybo' \
-e MYSQL_SERVICE_DB_NAME=nacos_config \
-e NACOS_SERVER_IP=192.168.200.130 \
-e JVM_XMS=256m \
-e JVM_XMX=256m \
-e JVM_XMN=128m \
-p 8848:8848 --restart=always \
--name nacos2  \
nacos/nacos-server:1.3.2