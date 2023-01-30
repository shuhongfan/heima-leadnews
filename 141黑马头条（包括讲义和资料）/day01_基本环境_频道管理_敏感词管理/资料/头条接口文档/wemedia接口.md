# 黑马头条API文档


**简介**:黑马头条API文档


**HOST**:localhost:6002


**联系人**:


**Version**:1.0


**接口路径**:/wemedia/v2/api-docs


[TOC]






# login-controller


## login


**接口地址**:`/wemedia/login/in`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"name": "",
	"password": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|WmUserDto|WmUserDto|
|&emsp;&emsp;name|||false|string||
|&emsp;&emsp;password|||false|string||


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


# wm-material-controller


## cancelCollectionMaterial


**接口地址**:`/wemedia/api/v1/material/cancel_collect/{id}`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|id|path|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResult|
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


## collectionMaterial


**接口地址**:`/wemedia/api/v1/material/collect/{id}`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|id|path|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResult|
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


## delPicture


**接口地址**:`/wemedia/api/v1/material/del_picture/{id}`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|id|path|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResult|
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


## findList


**接口地址**:`/wemedia/api/v1/material/list`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"isCollected": 0,
	"page": 0,
	"size": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|WmMaterialDto|WmMaterialDto|
|&emsp;&emsp;isCollected|||false|integer(int32)||
|&emsp;&emsp;page|当前第几页||false|integer(int32)||
|&emsp;&emsp;size|每页显示条数||false|integer(int32)||


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


## uploadPicture


**接口地址**:`/wemedia/api/v1/material/upload_picture`


**请求方式**:`POST`


**请求数据类型**:`multipart/form-data`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|multipartFile|multipartFile|formData|false|file||


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


# wm-news-controller


## delNews


**接口地址**:`/wemedia/api/v1/news/del_news/{id}`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|id|path|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResult|
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


## downOrUp


**接口地址**:`/wemedia/api/v1/news/down_or_up`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"channelId": 0,
	"content": "",
	"enable": 0,
	"id": 0,
	"images": [],
	"labels": "",
	"publishTime": "",
	"reason": "",
	"status": 0,
	"submitedTime": "",
	"title": "",
	"type": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|WmNewsDto|WmNewsDto|
|&emsp;&emsp;channelId|||false|integer(int32)||
|&emsp;&emsp;content|||false|string||
|&emsp;&emsp;enable|||false|integer(int32)||
|&emsp;&emsp;id|||false|integer(int32)||
|&emsp;&emsp;images|||false|array|string|
|&emsp;&emsp;labels|||false|string||
|&emsp;&emsp;publishTime|||false|string(date-time)||
|&emsp;&emsp;reason|||false|string||
|&emsp;&emsp;status|||false|integer(int32)||
|&emsp;&emsp;submitedTime|||false|string(date-time)||
|&emsp;&emsp;title|||false|string||
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


## findList


**接口地址**:`/wemedia/api/v1/news/findList`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"id": 0,
	"msg": "",
	"page": 0,
	"size": 0,
	"title": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|NewsAuthDto|NewsAuthDto|
|&emsp;&emsp;id|||false|integer(int32)||
|&emsp;&emsp;msg|||false|string||
|&emsp;&emsp;page|当前第几页||false|integer(int32)||
|&emsp;&emsp;size|每页显示条数||false|integer(int32)||
|&emsp;&emsp;title|||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|PageResponseResult|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|currentPage||integer(int32)|integer(int32)|
|data||object||
|errorMessage||string||
|host||string||
|size||integer(int32)|integer(int32)|
|total||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"code": 0,
	"currentPage": 0,
	"data": {},
	"errorMessage": "",
	"host": "",
	"size": 0,
	"total": 0
}
```


## findById


**接口地址**:`/wemedia/api/v1/news/findOne/{id}`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|id|path|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|WmNews|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|articleId||integer(int64)|integer(int64)|
|channelId||integer(int32)|integer(int32)|
|content||string||
|createdTime||string(date-time)|string(date-time)|
|enable||integer(int32)|integer(int32)|
|id||integer(int32)|integer(int32)|
|images||string||
|labels||string||
|publishTime||string(date-time)|string(date-time)|
|reason||string||
|status||integer(int32)|integer(int32)|
|submitedTime||string(date-time)|string(date-time)|
|title||string||
|type||integer(int32)|integer(int32)|
|userId||integer(int32)|integer(int32)|


**响应示例**:
```javascript
{
	"articleId": 0,
	"channelId": 0,
	"content": "",
	"createdTime": "",
	"enable": 0,
	"id": 0,
	"images": "",
	"labels": "",
	"publishTime": "",
	"reason": "",
	"status": 0,
	"submitedTime": "",
	"title": "",
	"type": 0,
	"userId": 0
}
```


## findRelease


**接口地址**:`/wemedia/api/v1/news/findRelease`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## findWmNewsVo


**接口地址**:`/wemedia/api/v1/news/find_news_vo/{id}`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|id|path|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|WmNewsVo|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|articleId||integer(int64)|integer(int64)|
|authorName||string||
|channelId||integer(int32)|integer(int32)|
|content||string||
|createdTime||string(date-time)|string(date-time)|
|enable||integer(int32)|integer(int32)|
|id||integer(int32)|integer(int32)|
|images||string||
|labels||string||
|publishTime||string(date-time)|string(date-time)|
|reason||string||
|status||integer(int32)|integer(int32)|
|submitedTime||string(date-time)|string(date-time)|
|title||string||
|type||integer(int32)|integer(int32)|
|userId||integer(int32)|integer(int32)|


**响应示例**:
```javascript
{
	"articleId": 0,
	"authorName": "",
	"channelId": 0,
	"content": "",
	"createdTime": "",
	"enable": 0,
	"id": 0,
	"images": "",
	"labels": "",
	"publishTime": "",
	"reason": "",
	"status": 0,
	"submitedTime": "",
	"title": "",
	"type": 0,
	"userId": 0
}
```


## findList


**接口地址**:`/wemedia/api/v1/news/list`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"beginPubDate": "",
	"channelId": 0,
	"endPubDate": "",
	"keyword": "",
	"page": 0,
	"size": 0,
	"status": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|WmNewsPageReqDto|WmNewsPageReqDto|
|&emsp;&emsp;beginPubDate|||false|string(date-time)||
|&emsp;&emsp;channelId|||false|integer(int32)||
|&emsp;&emsp;endPubDate|||false|string(date-time)||
|&emsp;&emsp;keyword|||false|string||
|&emsp;&emsp;page|当前第几页||false|integer(int32)||
|&emsp;&emsp;size|每页显示条数||false|integer(int32)||
|&emsp;&emsp;status|||false|integer(int32)||


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


## findWmNewsById


**接口地址**:`/wemedia/api/v1/news/one/{id}`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|id|path|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseResult|
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


## summitNews


**接口地址**:`/wemedia/api/v1/news/submit`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"channelId": 0,
	"content": "",
	"enable": 0,
	"id": 0,
	"images": [],
	"labels": "",
	"publishTime": "",
	"reason": "",
	"status": 0,
	"submitedTime": "",
	"title": "",
	"type": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|wmNews|wmNews|body|true|WmNewsDto|WmNewsDto|
|&emsp;&emsp;channelId|||false|integer(int32)||
|&emsp;&emsp;content|||false|string||
|&emsp;&emsp;enable|||false|integer(int32)||
|&emsp;&emsp;id|||false|integer(int32)||
|&emsp;&emsp;images|||false|array|string|
|&emsp;&emsp;labels|||false|string||
|&emsp;&emsp;publishTime|||false|string(date-time)||
|&emsp;&emsp;reason|||false|string||
|&emsp;&emsp;status|||false|integer(int32)||
|&emsp;&emsp;submitedTime|||false|string(date-time)||
|&emsp;&emsp;title|||false|string||
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


## updateWmNews


**接口地址**:`/wemedia/api/v1/news/update`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"articleId": 0,
	"channelId": 0,
	"content": "",
	"createdTime": "",
	"enable": 0,
	"id": 0,
	"images": "",
	"labels": "",
	"publishTime": "",
	"reason": "",
	"status": 0,
	"submitedTime": "",
	"title": "",
	"type": 0,
	"userId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|wmNews|wmNews|body|true|WmNews|WmNews|
|&emsp;&emsp;articleId|||false|integer(int64)||
|&emsp;&emsp;channelId|||false|integer(int32)||
|&emsp;&emsp;content|||false|string||
|&emsp;&emsp;createdTime|||false|string(date-time)||
|&emsp;&emsp;enable|||false|integer(int32)||
|&emsp;&emsp;id|||false|integer(int32)||
|&emsp;&emsp;images|||false|string||
|&emsp;&emsp;labels|||false|string||
|&emsp;&emsp;publishTime|||false|string(date-time)||
|&emsp;&emsp;reason|||false|string||
|&emsp;&emsp;status|||false|integer(int32)||
|&emsp;&emsp;submitedTime|||false|string(date-time)||
|&emsp;&emsp;title|||false|string||
|&emsp;&emsp;type|||false|integer(int32)||
|&emsp;&emsp;userId|||false|integer(int32)||


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


# wm-user-controller


## 根据用户名查询自媒体用户


**接口地址**:`/wemedia/api/v1/user/findByName/{name}`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|name|name|path|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|WmUser|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|apAuthorId||integer(int32)|integer(int32)|
|apUserId||integer(int32)|integer(int32)|
|createdTime||string(date-time)|string(date-time)|
|email||string||
|id||integer(int32)|integer(int32)|
|image||string||
|location||string||
|loginTime||string(date-time)|string(date-time)|
|name||string||
|nickname||string||
|password||string||
|phone||string||
|salt||string||
|score||integer(int32)|integer(int32)|
|status||integer(int32)|integer(int32)|
|type||integer(int32)|integer(int32)|


**响应示例**:
```javascript
{
	"apAuthorId": 0,
	"apUserId": 0,
	"createdTime": "",
	"email": "",
	"id": 0,
	"image": "",
	"location": "",
	"loginTime": "",
	"name": "",
	"nickname": "",
	"password": "",
	"phone": "",
	"salt": "",
	"score": 0,
	"status": 0,
	"type": 0
}
```


## findWmUserById


**接口地址**:`/wemedia/api/v1/user/findOne/{id}`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|id|path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|WmUser|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|apAuthorId||integer(int32)|integer(int32)|
|apUserId||integer(int32)|integer(int32)|
|createdTime||string(date-time)|string(date-time)|
|email||string||
|id||integer(int32)|integer(int32)|
|image||string||
|location||string||
|loginTime||string(date-time)|string(date-time)|
|name||string||
|nickname||string||
|password||string||
|phone||string||
|salt||string||
|score||integer(int32)|integer(int32)|
|status||integer(int32)|integer(int32)|
|type||integer(int32)|integer(int32)|


**响应示例**:
```javascript
{
	"apAuthorId": 0,
	"apUserId": 0,
	"createdTime": "",
	"email": "",
	"id": 0,
	"image": "",
	"location": "",
	"loginTime": "",
	"name": "",
	"nickname": "",
	"password": "",
	"phone": "",
	"salt": "",
	"score": 0,
	"status": 0,
	"type": 0
}
```


## 保存自媒体用户


**接口地址**:`/wemedia/api/v1/user/save`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"apAuthorId": 0,
	"apUserId": 0,
	"createdTime": "",
	"email": "",
	"id": 0,
	"image": "",
	"location": "",
	"loginTime": "",
	"name": "",
	"nickname": "",
	"password": "",
	"phone": "",
	"salt": "",
	"score": 0,
	"status": 0,
	"type": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|wmUser|wmUser|body|true|WmUser|WmUser|
|&emsp;&emsp;apAuthorId|||false|integer(int32)||
|&emsp;&emsp;apUserId|||false|integer(int32)||
|&emsp;&emsp;createdTime|||false|string(date-time)||
|&emsp;&emsp;email|||false|string||
|&emsp;&emsp;id|||false|integer(int32)||
|&emsp;&emsp;image|||false|string||
|&emsp;&emsp;location|||false|string||
|&emsp;&emsp;loginTime|||false|string(date-time)||
|&emsp;&emsp;name|||false|string||
|&emsp;&emsp;nickname|||false|string||
|&emsp;&emsp;password|||false|string||
|&emsp;&emsp;phone|||false|string||
|&emsp;&emsp;salt|||false|string||
|&emsp;&emsp;score|||false|integer(int32)||
|&emsp;&emsp;status|||false|integer(int32)||
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