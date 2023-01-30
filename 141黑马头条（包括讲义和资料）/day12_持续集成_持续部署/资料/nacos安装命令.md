```
docker run -d \
-e PREFER_HOST_MODE=hostname \
-e MODE=standalone \
-e SPRING_DATASOURCE_PLATFORM=mysql \
-e MYSQL_SERVICE_HOST=192.168.200.130 \
-e MYSQL_SERVICE_PORT=3306 \
-e MYSQL_SERVICE_USER=root \
-e MYSQL_SERVICE_PASSWORD=root \
-e MYSQL_SERVICE_DB_NAME=nacos_config \
-e NACOS_SERVER_IP=192.168.200.130 \
-e JVM_XMS=256m \
-e JVM_XMX=256m \
-e JVM_XMN=128m \
-p 8848:8848 \
--name nacos \
nacos/nacos-server:1.3.2
```

