# 黑马头条API文档


**简介**:黑马头条API文档


**HOST**:localhost:5001


**联系人**:


**Version**:1.0


**接口路径**:/behavior/v2/api-docs


[TOC]






# ap-behavior-entry-controller


## findByUserIdOrEquipmentId


**接口地址**:`/behavior/api/v1/behavior_entry/one`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|equipmentId|equipmentId|query|true|integer(int32)||
|userId|userId|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ApBehaviorEntry|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|createdTime||string(date-time)|string(date-time)|
|entryId||integer(int32)|integer(int32)|
|id||integer(int32)|integer(int32)|
|type||integer(int32)|integer(int32)|
|user||boolean||


**响应示例**:
```javascript
{
	"createdTime": "",
	"entryId": 0,
	"id": 0,
	"type": 0,
	"user": true
}
```


# ap-likes-behavior-controller


## like


**接口地址**:`/behavior/api/v1/likes_behavior`


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
|dto|dto|body|true|LikesBehaviorDto|LikesBehaviorDto|
|&emsp;&emsp;articleId|||false|integer(int64)||
|&emsp;&emsp;equipmentId|||false|integer(int32)||
|&emsp;&emsp;operation|||false|integer(int32)||
|&emsp;&emsp;type|||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResult|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


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


## findLikeByArticleIdAndEntryId


**接口地址**:`/behavior/api/v1/likes_behavior/one`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|articleId|articleId|query|true|integer(int64)||
|entryId|entryId|query|true|integer(int32)||
|type|type|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ApLikesBehavior|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|articleId||integer(int64)|integer(int64)|
|createdTime||string(date-time)|string(date-time)|
|entryId||integer(int32)|integer(int32)|
|id||integer(int64)|integer(int64)|
|operation||integer(int32)|integer(int32)|
|type||integer(int32)|integer(int32)|


**响应示例**:
```javascript
{
	"articleId": 0,
	"createdTime": "",
	"entryId": 0,
	"id": 0,
	"operation": 0,
	"type": 0
}
```


# ap-read-behavior-controller


## readBehavior


**接口地址**:`/behavior/api/v1/read_behavior`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"articleId": 0,
	"count": 0,
	"equipmentId": 0,
	"loadDuration": 0,
	"percentage": 0,
	"readDuration": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|ReadBehaviorDto|ReadBehaviorDto|
|&emsp;&emsp;articleId|||false|integer(int64)||
|&emsp;&emsp;count|||false|integer(int32)||
|&emsp;&emsp;equipmentId|||false|integer(int32)||
|&emsp;&emsp;loadDuration|||false|integer(int32)||
|&emsp;&emsp;percentage|||false|integer(int32)||
|&emsp;&emsp;readDuration|||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResult|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


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


# ap-unlikes-behavior-controller


## findUnLikeByArticleIdAndEntryId


**接口地址**:`/behavior/api/v1/un_likes_behavior/one`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|articleId|articleId|query|true|integer(int64)||
|entryId|entryId|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ApUnlikesBehavior|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|articleId||integer(int64)|integer(int64)|
|createdTime||string(date-time)|string(date-time)|
|entryId||integer(int32)|integer(int32)|
|id||integer(int64)|integer(int64)|
|type||integer(int32)|integer(int32)|


**响应示例**:
```javascript
{
	"articleId": 0,
	"createdTime": "",
	"entryId": 0,
	"id": 0,
	"type": 0
}
```