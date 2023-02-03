#### day04_发表文章

**Stream API**

```
创建流 :

	集合.stream()
    
    Arrays.stream(数组)
    
    Stream.of(数据1,2,3,4,5,6);

中间操作: 
     
    filter ( 表达式 )
    
    map   映射 
    
    distinct  去重     hashcode  equals
    
    limit    截取
    
    skip    跳过
    
    sorted  排序  

终止操作:  

    foreach  遍历流 
    
    collect  收集
    
            Collectors. toList         收集到一个集合   
            			toMap(key,value)          转成集合
                        joining("拼接符")        拼接成一个字符串
                        groupingBy     根据字段值分组 Map<String,List<>>
    
    .reduce   归并   [1,2,3,4,5,6,7]
    .reduce(a,b)
```

**发表文章**

```
基本业务流程:   自媒体人可以在发表文章页面中填写文章信息,   title  content[文本，图片]  images封面
              type 布局(-1 自动生成封面)   pulishTime （发布时间）,提交到后台   后台将数据保存到
              wm_news表中 此时文章状态 ( 0 草稿    1 待审核 ),如果是待审状态需要报错 素材和文章的               关联关系  到 wm_news_material
涉及表:   wm_news  文章   wm_material 素材   wm_news_material 文章素材关联表
 
具体流程:   将将文章信息 封装成一个大的json传入后台，后台封装dto实体类来接收
   
              1. 校验参数  并转为wmNews实体对象
                   标题  内容 不能为空
                   是否登陆
                   type == -1 代表自动
                   images (集合) ==>  封面(str)
              2.  保存或修改文章 
              		如果 id存在:
                        删除文章和素材的关联关系
              		    修改文章
              	    如果 id不存在 
              	        直接保存
              3.   保存文章和素材关联关系
              
              3.1    解析出内容中的引用的素材
                 
                    content: [{type:text     value: 文本  },{type:image,value: 素材url},{},{}]
              3.2    保存内容引用素材关联关系
                      
                      foreach  
                      select id from wm_material where url in (素材url路径集合) and user_id = ?
                      
                      素材ids集合
                      foreach 标签
                      insert into 表 (字段) values (值),(值),(值)
              
 			  3.3    保存封面引用素材关联关系
 			  
 			          如果type == -1 需要先生成封面
 			               
 			              	   如果内容图片长度大于2
 			                   取 3个作为封面    多图
 			                   
 			                   如果内容图片长度大于0  小于等于2
 			                   取 1个作为封面    单图
 			                   
 			                   其它
 			                   无图
```







