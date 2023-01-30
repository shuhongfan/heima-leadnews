# 黑马头条API文档


**简介**:黑马头条API文档


**HOST**:localhost:9005


**联系人**:


**Version**:1.0


**接口路径**:/v2/api-docs?group=1.0


[TOC]






# 点赞行为API


## 保存或取消 点赞行为

**接口地址**:`/api/v1/likes_behavior`

**请求方式**:`POST`

**请求数据类型**:`application/json`


**响应数据类型**:`*/*`

**接口描述**:  用户在文章详情页面，点赞需要保存赞的行为，取消赞需要删除赞的行为，点赞行为需要存储在mongo中   ，当前用户必须登录才可以点赞，不可以重复点赞




**请求示例**:


```javascript
{
	"articleId": 0,
	"equipmentId": 0,
	"operation": 0,
	"type": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|LikesBehaviorDTO|                  |
|&emsp;&emsp;articleId|文章id||true|integer(int64)||
|&emsp;&emsp;equipmentId|设备id||true|integer(int32)||
|&emsp;&emsp;operation|操作||true|integer(int32)|0 点赞  1 取消赞|
|&emsp;&emsp;type|类型||false|integer(int32)|0 文章  1 动态|

**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- |
|code||integer(int32)|integer(int32)|
|data||object||
|errorMessage||string||
|host||string||

**响应示例**:

```javascript
{
	"code": 0,
	"data": {},
	"errorMessage": "",
	"host": ""
}
```


# 阅读行为API


## 保存阅读行为

**接口地址**:`/api/v1/read_behavior`

**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`

**接口描述**:  每次用户阅读文章，需要添加阅读行为，如果对应行为存在 累加阅读次数，如果用户没登录根据设备关联的行为实体数据进行保存

**请求示例**:


```javascript
{
	"articleId": 0, 
	"count": 0, // 不需要
	"equipmentId": 0,
	"loadDuration": 0, // 加载时长，前端传入时间不准 不需要
	"percentage": 0, // 阅读百分比 ， 不需要
	"readDuration": 0 // 阅读持续时长，不需要
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|ReadBehaviorDto|ReadBehaviorDto|
|&emsp;&emsp;articleId|文章id||false|integer(int64)||
|&emsp;&emsp;equipmentId|||false|integer(int32)||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- |
|code||integer(int32)|integer(int32)|
|data||object||
|errorMessage||string||
|host||string||


**响应示例**:
```javascript
q
```


# 不喜欢行为API


## 保存或取消 不喜欢行为

**接口地址**:`/api/v1/un_likes_behavior/`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"articleId": 0,
	"equipmentId": 0,
	"type": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|UnLikesBehaviorDto|UnLikesBehaviorDto|
|&emsp;&emsp;articleId|文章id||true|integer(int64)||
|&emsp;&emsp;equipmentId|设备id||false|integer(int32)||
|&emsp;&emsp;type|操作类型||true|integer(int32)|0 不喜欢  1 取消不喜欢|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- |
|code||integer(int32)|integer(int32)|
|data||object||
|errorMessage||string||
|host||string||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"errorMessage": "",
	"host": ""
}
```


# 收藏行为API


## 保存或取消 收藏行为


**接口地址**:`/api/v1/collection_behavior/`

**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"articleId": 0,
	"equipmentId": 0,
	"operation": 0,
	"type": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|CollectionBehaviorDto|CollectionBehaviorDto|
|&emsp;&emsp;articleId|文章id||true|integer(int64)||
|&emsp;&emsp;equipmentId|设备id||true|integer(int32)||
|&emsp;&emsp;operation|操作||true|integer(int32)|0 收藏 1 取消收藏|
|&emsp;&emsp;type|类型||true|integer(int32)|0 文章  1 动态|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- |
|code||integer(int32)|integer(int32)|
|data||object||
|errorMessage||string||
|host||string||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"errorMessage": "",
	"host": ""
}
```


# 文章行为api


## 查询文章行为相关信息

**接口地址**:`/api/v1/article/load_article_behavior`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"articleId": 0,
	"authorApUserId": 0,
	"authorId": 0,
	"equipmentId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|ArticleBehaviorDto|ArticleBehaviorDto|
|&emsp;&emsp;articleId|文章id||true|integer(int64)||
|&emsp;&emsp;authorApUserId|作者userId||true|integer(int32)||
|&emsp;&emsp;authorId|||false|integer(int32)||
|&emsp;&emsp;equipmentId|设备id||true|integer(int32)||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- |
|code||integer(int32)|integer(int32)|
|data||object||
|&emsp;&emsp;&emsp;islike||boolean|是否点赞|
|&emsp;&emsp;&emsp;isfollow||boolean|是否关注|
|&emsp;&emsp;&emsp;isunlike||boolean|是否不喜欢|
|&emsp;&emsp;&emsp;iscollection||boolean|是否收藏|
|errorMessage||string||
|host||string||

**响应示例**:

```javascript
{
	"code": 0,
	"data": {"isfollow":true,"islike":true,"isunlike":false,"iscollection":true},
	"errorMessage": "",
	"host": ""
}
```