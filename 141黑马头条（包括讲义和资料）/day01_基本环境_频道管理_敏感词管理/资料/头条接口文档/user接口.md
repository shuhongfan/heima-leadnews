# 黑马头条API文档


**简介**:黑马头条API文档


**HOST**:localhost:6001


**联系人**:


**Version**:1.0


**接口路径**:/user/v2/api-docs


[TOC]






# ap-user-controller


## findUserById


**接口地址**:`/user/api/v1/user/{id}`


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
|200|OK|ApUser|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|certification||boolean||
|createdTime||string(date-time)|string(date-time)|
|flag||boolean||
|id||integer(int32)|integer(int32)|
|identityAuthentication||boolean||
|image||string||
|name||string||
|password||string||
|phone||string||
|salt||string||
|sex||boolean||
|status||boolean||


**响应示例**:
```javascript
{
	"certification": true,
	"createdTime": "",
	"flag": true,
	"id": 0,
	"identityAuthentication": true,
	"image": "",
	"name": "",
	"password": "",
	"phone": "",
	"salt": "",
	"sex": true,
	"status": true
}
```


# ap-user-follow-controller


## findByUserIdAndFollowId


**接口地址**:`/user/api/v1/user_follow/one`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|followId|followId|query|true|integer(int32)||
|userId|userId|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ApUserFollow|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|createdTime||string(date-time)|string(date-time)|
|followId||integer(int32)|integer(int32)|
|followName||string||
|id||integer(int32)|integer(int32)|
|isNotice||boolean||
|level||integer(int32)|integer(int32)|
|userId||integer(int32)|integer(int32)|


**响应示例**:
```javascript
{
	"createdTime": "",
	"followId": 0,
	"followName": "",
	"id": 0,
	"isNotice": true,
	"level": 0,
	"userId": 0
}
```


# ap-user-login-controller


## login


**接口地址**:`/user/api/v1/login/login_auth`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"equipmentId": 0,
	"password": "",
	"phone": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|LoginDto|LoginDto|
|&emsp;&emsp;equipmentId|||false|integer(int32)||
|&emsp;&emsp;password|||false|string||
|&emsp;&emsp;phone|||false|string||


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


# user-relation-controller


## follow


**接口地址**:`/user/api/v1/user/user_follow`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"articleId": 0,
	"authorId": 0,
	"operation": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|UserRelationDto|UserRelationDto|
|&emsp;&emsp;articleId|||false|integer(int64)||
|&emsp;&emsp;authorId|||false|integer(int32)||
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


# 实名认证控制类api


## authFail


**接口地址**:`/user/api/v1/auth/authFail`


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
	"status": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|AuthDto|AuthDto|
|&emsp;&emsp;id|||false|integer(int32)||
|&emsp;&emsp;msg|||false|string||
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


## authPass


**接口地址**:`/user/api/v1/auth/authPass`


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
	"status": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|AuthDto|AuthDto|
|&emsp;&emsp;id|||false|integer(int32)||
|&emsp;&emsp;msg|||false|string||
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


## 根据状态查询实名认证列表


**接口地址**:`/user/api/v1/auth/list`


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
	"status": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|AuthDto|AuthDto|
|&emsp;&emsp;id|||false|integer(int32)||
|&emsp;&emsp;msg|||false|string||
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