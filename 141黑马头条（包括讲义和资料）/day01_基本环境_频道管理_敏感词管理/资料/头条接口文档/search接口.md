# 黑马头条API文档


**简介**:黑马头条API文档


**HOST**:localhost:5001


**联系人**:


**Version**:1.0


**接口路径**:/search/v2/api-docs


[TOC]






# ap-associate-words-controller


## search


**接口地址**:`/search/api/v1/associate/search`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"equipmentId": 0,
	"fromIndex": 0,
	"id": 0,
	"minBehotTime": "",
	"pageNum": 0,
	"pageSize": 0,
	"searchWords": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userSearchDto|userSearchDto|body|true|UserSearchDto|UserSearchDto|
|&emsp;&emsp;equipmentId|||false|integer(int32)||
|&emsp;&emsp;fromIndex|||false|integer(int32)||
|&emsp;&emsp;id|||false|integer(int32)||
|&emsp;&emsp;minBehotTime|||false|string(date-time)||
|&emsp;&emsp;pageNum|||false|integer(int32)||
|&emsp;&emsp;pageSize|||false|integer(int32)||
|&emsp;&emsp;searchWords|||false|string||


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


# ap-user-search-controller


## delUserSearch


**接口地址**:`/search/api/v1/history/del`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"equipmentId": 0,
	"fromIndex": 0,
	"id": 0,
	"minBehotTime": "",
	"pageNum": 0,
	"pageSize": 0,
	"searchWords": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userSearchDto|userSearchDto|body|true|UserSearchDto|UserSearchDto|
|&emsp;&emsp;equipmentId|||false|integer(int32)||
|&emsp;&emsp;fromIndex|||false|integer(int32)||
|&emsp;&emsp;id|||false|integer(int32)||
|&emsp;&emsp;minBehotTime|||false|string(date-time)||
|&emsp;&emsp;pageNum|||false|integer(int32)||
|&emsp;&emsp;pageSize|||false|integer(int32)||
|&emsp;&emsp;searchWords|||false|string||


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


## findUserSearch


**接口地址**:`/search/api/v1/history/load`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"equipmentId": 0,
	"fromIndex": 0,
	"id": 0,
	"minBehotTime": "",
	"pageNum": 0,
	"pageSize": 0,
	"searchWords": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userSearchDto|userSearchDto|body|true|UserSearchDto|UserSearchDto|
|&emsp;&emsp;equipmentId|||false|integer(int32)||
|&emsp;&emsp;fromIndex|||false|integer(int32)||
|&emsp;&emsp;id|||false|integer(int32)||
|&emsp;&emsp;minBehotTime|||false|string(date-time)||
|&emsp;&emsp;pageNum|||false|integer(int32)||
|&emsp;&emsp;pageSize|||false|integer(int32)||
|&emsp;&emsp;searchWords|||false|string||


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


# article-search-controller


## search


**接口地址**:`/search/api/v1/article/search/search`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"equipmentId": 0,
	"fromIndex": 0,
	"minBehotTime": "",
	"pageNum": 0,
	"pageSize": 0,
	"searchWords": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userSearchDto|userSearchDto|body|true|UserSearchDto|UserSearchDto|
|&emsp;&emsp;equipmentId|||false|integer(int32)||
|&emsp;&emsp;fromIndex|||false|integer(int32)||
|&emsp;&emsp;minBehotTime|||false|string(date-time)||
|&emsp;&emsp;pageNum|||false|integer(int32)||
|&emsp;&emsp;pageSize|||false|integer(int32)||
|&emsp;&emsp;searchWords|||false|string||


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