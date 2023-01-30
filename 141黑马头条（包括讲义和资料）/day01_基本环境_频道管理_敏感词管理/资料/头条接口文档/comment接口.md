# 黑马头条API文档


**简介**:黑马头条API文档


**HOST**:localhost:5001


**联系人**:


**Version**:1.0


**接口路径**:/comment/v2/api-docs


[TOC]






# comment-controller


## like


**接口地址**:`/comment/api/v1/comment/like`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"commentId": "",
	"operation": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|CommentLikeDto|CommentLikeDto|
|&emsp;&emsp;commentId|||false|string||
|&emsp;&emsp;operation|||false|integer(int32)||


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


## findByArticleId


**接口地址**:`/comment/api/v1/comment/load`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"articleId": 0,
	"index": 0,
	"minDate": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|CommentDto|CommentDto|
|&emsp;&emsp;articleId|||false|integer(int64)||
|&emsp;&emsp;index|||false|integer(int32)||
|&emsp;&emsp;minDate|||false|string(date-time)||


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


## saveComment


**接口地址**:`/comment/api/v1/comment/save`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"articleId": 0,
	"content": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|CommentSaveDto|CommentSaveDto|
|&emsp;&emsp;articleId|||false|integer(int64)||
|&emsp;&emsp;content|||false|string||


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


# comment-repay-controller


## saveCommentRepayLike


**接口地址**:`/comment/api/v1/comment_repay/like`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"commentRepayId": "",
	"operation": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|CommentRepayLikeDto|CommentRepayLikeDto|
|&emsp;&emsp;commentRepayId|||false|string||
|&emsp;&emsp;operation|||false|integer(int32)||


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


## loadCommentRepay


**接口地址**:`/comment/api/v1/comment_repay/load`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"commentId": "",
	"minDate": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|CommentRepayDto|CommentRepayDto|
|&emsp;&emsp;commentId|||false|string||
|&emsp;&emsp;minDate|||false|string(date-time)||


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


## saveCommentRepay


**接口地址**:`/comment/api/v1/comment_repay/save`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"commentId": "",
	"content": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|CommentRepaySaveDto|CommentRepaySaveDto|
|&emsp;&emsp;commentId|||false|string||
|&emsp;&emsp;content|||false|string||


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