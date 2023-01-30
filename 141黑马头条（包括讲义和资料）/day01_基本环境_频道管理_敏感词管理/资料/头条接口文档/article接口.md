# 黑马头条API文档


**简介**:黑马头条API文档


**HOST**:localhost:5001


**联系人**:


**Version**:1.0


**接口路径**:/article/v2/api-docs


[TOC]






# ap-article-config-controller


## saveArticleConfig


**接口地址**:`/article/api/v1/article_config/save`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"articleId": 0,
	"id": 0,
	"isComment": true,
	"isDelete": true,
	"isDown": true,
	"isForward": true
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|apArticleConfig|apArticleConfig|body|true|ApArticleConfig|ApArticleConfig|
|&emsp;&emsp;articleId|||false|integer(int64)||
|&emsp;&emsp;id|||false|integer(int64)||
|&emsp;&emsp;isComment|||false|boolean||
|&emsp;&emsp;isDelete|||false|boolean||
|&emsp;&emsp;isDown|||false|boolean||
|&emsp;&emsp;isForward|||false|boolean||


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


# ap-article-content-controller


## saveArticleContent


**接口地址**:`/article/api/v1/article_content/save`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"articleId": 0,
	"content": "",
	"id": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|apArticleContent|apArticleContent|body|true|ApArticleContent|ApArticleContent|
|&emsp;&emsp;articleId|||false|integer(int64)||
|&emsp;&emsp;content|||false|string||
|&emsp;&emsp;id|||false|integer(int64)||


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


# ap-article-controller


## saveArticle


**接口地址**:`/article/api/v1/article/save`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"authorId": 0,
	"authorName": "",
	"channelId": 0,
	"channelName": "",
	"cityId": 0,
	"collection": 0,
	"comment": 0,
	"countyId": 0,
	"createdTime": "",
	"flag": 0,
	"id": 0,
	"images": "",
	"labels": "",
	"layout": 0,
	"likes": 0,
	"origin": true,
	"provinceId": 0,
	"publishTime": "",
	"syncStatus": true,
	"title": "",
	"views": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|apArticle|apArticle|body|true|ApArticle|ApArticle|
|&emsp;&emsp;authorId|||false|integer(int64)||
|&emsp;&emsp;authorName|||false|string||
|&emsp;&emsp;channelId|||false|integer(int32)||
|&emsp;&emsp;channelName|||false|string||
|&emsp;&emsp;cityId|||false|integer(int32)||
|&emsp;&emsp;collection|||false|integer(int32)||
|&emsp;&emsp;comment|||false|integer(int32)||
|&emsp;&emsp;countyId|||false|integer(int32)||
|&emsp;&emsp;createdTime|||false|string(date-time)||
|&emsp;&emsp;flag|||false|integer(int32)||
|&emsp;&emsp;id|||false|integer(int64)||
|&emsp;&emsp;images|||false|string||
|&emsp;&emsp;labels|||false|string||
|&emsp;&emsp;layout|||false|integer(int32)||
|&emsp;&emsp;likes|||false|integer(int32)||
|&emsp;&emsp;origin|||false|boolean||
|&emsp;&emsp;provinceId|||false|integer(int32)||
|&emsp;&emsp;publishTime|||false|string(date-time)||
|&emsp;&emsp;syncStatus|||false|boolean||
|&emsp;&emsp;title|||false|string||
|&emsp;&emsp;views|||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ApArticle|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|authorId||integer(int64)|integer(int64)|
|authorName||string||
|channelId||integer(int32)|integer(int32)|
|channelName||string||
|cityId||integer(int32)|integer(int32)|
|collection||integer(int32)|integer(int32)|
|comment||integer(int32)|integer(int32)|
|countyId||integer(int32)|integer(int32)|
|createdTime||string(date-time)|string(date-time)|
|flag||integer(int32)|integer(int32)|
|id||integer(int64)|integer(int64)|
|images||string||
|labels||string||
|layout||integer(int32)|integer(int32)|
|likes||integer(int32)|integer(int32)|
|origin||boolean||
|provinceId||integer(int32)|integer(int32)|
|publishTime||string(date-time)|string(date-time)|
|syncStatus||boolean||
|title||string||
|views||integer(int32)|integer(int32)|


**响应示例**:
```javascript
{
	"authorId": 0,
	"authorName": "",
	"channelId": 0,
	"channelName": "",
	"cityId": 0,
	"collection": 0,
	"comment": 0,
	"countyId": 0,
	"createdTime": "",
	"flag": 0,
	"id": 0,
	"images": "",
	"labels": "",
	"layout": 0,
	"likes": 0,
	"origin": true,
	"provinceId": 0,
	"publishTime": "",
	"syncStatus": true,
	"title": "",
	"views": 0
}
```


## findArticle


**接口地址**:`/article/api/v1/article/{id}`


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
|200|OK||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


暂无


**响应示例**:
```javascript

```


# app端作者控制类api


## 根据apUserId查询关联作者信息


**接口地址**:`/article/api/v1/author/findByUserId/{id}`


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
|200|OK|ApAuthor|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|createdTime||string(date-time)|string(date-time)|
|id||integer(int32)|integer(int32)|
|name||string||
|type||integer(int32)|integer(int32)|
|userId||integer(int32)|integer(int32)|
|wmUserId||integer(int32)|integer(int32)|


**响应示例**:
```javascript
{
	"createdTime": "",
	"id": 0,
	"name": "",
	"type": 0,
	"userId": 0,
	"wmUserId": 0
}
```


## findById


**接口地址**:`/article/api/v1/author/one/{id}`


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
|200|OK|ApAuthor|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|createdTime||string(date-time)|string(date-time)|
|id||integer(int32)|integer(int32)|
|name||string||
|type||integer(int32)|integer(int32)|
|userId||integer(int32)|integer(int32)|
|wmUserId||integer(int32)|integer(int32)|


**响应示例**:
```javascript
{
	"createdTime": "",
	"id": 0,
	"name": "",
	"type": 0,
	"userId": 0,
	"wmUserId": 0
}
```


## 保存作者信息


**接口地址**:`/article/api/v1/author/save`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"createdTime": "",
	"id": 0,
	"name": "",
	"type": 0,
	"userId": 0,
	"wmUserId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|apAuthor|apAuthor|body|true|ApAuthor|ApAuthor|
|&emsp;&emsp;createdTime|||false|string(date-time)||
|&emsp;&emsp;id|||false|integer(int32)||
|&emsp;&emsp;name|||false|string||
|&emsp;&emsp;type|||false|integer(int32)||
|&emsp;&emsp;userId|||false|integer(int32)||
|&emsp;&emsp;wmUserId|||false|integer(int32)||


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


# article-home-controller


## load


**接口地址**:`/article/api/v1/article/load`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"maxBehotTime": "",
	"minBehotTime": "",
	"size": 0,
	"tag": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|ArticleHomeDto|ArticleHomeDto|
|&emsp;&emsp;maxBehotTime|||false|string(date-time)||
|&emsp;&emsp;minBehotTime|||false|string(date-time)||
|&emsp;&emsp;size|||false|integer(int32)||
|&emsp;&emsp;tag|||false|string||


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


## loadMore


**接口地址**:`/article/api/v1/article/loadmore`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"maxBehotTime": "",
	"minBehotTime": "",
	"size": 0,
	"tag": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|ArticleHomeDto|ArticleHomeDto|
|&emsp;&emsp;maxBehotTime|||false|string(date-time)||
|&emsp;&emsp;minBehotTime|||false|string(date-time)||
|&emsp;&emsp;size|||false|integer(int32)||
|&emsp;&emsp;tag|||false|string||


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


## loadNew


**接口地址**:`/article/api/v1/article/loadnew`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"maxBehotTime": "",
	"minBehotTime": "",
	"size": 0,
	"tag": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|ArticleHomeDto|ArticleHomeDto|
|&emsp;&emsp;maxBehotTime|||false|string(date-time)||
|&emsp;&emsp;minBehotTime|||false|string(date-time)||
|&emsp;&emsp;size|||false|integer(int32)||
|&emsp;&emsp;tag|||false|string||


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


# article-info-controller


## loadArticleBehavior


**接口地址**:`/article/api/v1/article/load_article_behavior`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"articleId": 0,
	"authorId": 0,
	"equipmentId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|ArticleInfoDto|ArticleInfoDto|
|&emsp;&emsp;articleId|||false|integer(int64)||
|&emsp;&emsp;authorId|||false|integer(int32)||
|&emsp;&emsp;equipmentId|||false|integer(int32)||


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


## loadArticleInfo


**接口地址**:`/article/api/v1/article/load_article_info`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"articleId": 0,
	"authorId": 0,
	"equipmentId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|ArticleInfoDto|ArticleInfoDto|
|&emsp;&emsp;articleId|||false|integer(int64)||
|&emsp;&emsp;authorId|||false|integer(int32)||
|&emsp;&emsp;equipmentId|||false|integer(int32)||


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