#### day07快速回顾



**app端登陆**

```
登陆
    phone   password
       查询用户  对比密码
       颁发token
    
不登陆
	设备id 
	   
	直接颁发 token
```

**app文章列表查询**

```
app端的文章列表:

入参:   频道id  size   最小时间   最大时间

服务实现:

       ap_article     ap_article_config 排除 删除    下架
       
       查询更多   发布时间 < 最小时间
       
       查询最新   发布时间 > 最大时间
       
       频道id  =  channel_id
       
       排序 发布时间降序
       
       分页
```

**文章页面静态化**

```  
文章详情， 如果直接根据文章id查询数据库的话，数据压力会很大，无法应对高并发，所以 采取页面静态化的方案

实现流程:  
    1. 发布文章时，如果发布成功 需要生成静态页
    
    2. 基于freemarker语法，提前准备好文章详情的模板 
    		${变量}   <#list  集合 as 变量>   <#if 变量?? >    ${时间变量?datetime} 
    
    3. 实现生成静态页方法
    
        3.1  获取模板对象  template
        
        3.2  准备替换模板的数据
        		${authorApUserId?c}
        		<#if article??>
        		 <#list content as item>
        3.3  替换模板输出内容   输出==>   new StringWriter();
        
        3.4  获取输入流准备上传到minio   new ByteArrayInputStream(字符串的.bytes())
        
        3.5  上传到minio中   filestrorageservice.store(     "text/html")
        
        3.6  得到minio中html的路径 设置到 article表的static_url字段上
    4. 在app首页访问文章详情时，会直接访问static_url的地址
```

**关注取关**

```
app详情页面，  可以通过右上角的关注 或 取关 按钮  来关注或者取消关注作者

如果采用数据库的方式实现，性能特别差 .  我们选择采用redis实现

redis:  key value 的 基于内存的nosql数据库  【string   hash   set   zset   list】

关注功能:   1 对 多  (集合)    不能重复关注    按照关注时间 有序  

           所以选择:   zset        有序 不重复的  set集合
           
使用命令:    zadd  follow:登陆用户id  时间戳  作者id     关注

           zrem   follow:登陆用户id  作者id   取关
           
           zrange  follow:登陆用户id  0  -1    查询所有关注信息
           
           zscore  follow:登陆用户id  作者id    是否关注过该用户
           
代码实现:  redisTemplate 操作的redis

         operation 0  关注    1  取关
         
         
         关注 ：  不能自己关注自己  不能重复关注
           
                 关注集合  添加数据
                 
                 粉丝集合  添加数据
                 
                 
         取关:
                 关注集合  删除数据
                 
                 粉丝集合  删除数据
           
           
           
           
```

