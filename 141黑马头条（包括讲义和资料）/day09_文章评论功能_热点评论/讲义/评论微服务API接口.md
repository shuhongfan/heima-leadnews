# 黑马头条API文档


**简介**:黑马头条API文档


**HOST**:localhost:9006


**联系人**:


**Version**:1.0


**接口路径**:/v2/api-docs?group=1.0


[TOC]






# 评论管理API


## 保存评论点赞

**接口地址**:`/api/v1/comment/like`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"commentId": "", // 被点赞的评论id   不能为空
	"operation": 0  // 点赞类型 0 点赞  1 取消赞  只能 0 1
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|CommentLikeDto|CommentLikeDto|
|&emsp;&emsp;commentId|||true|string||
|&emsp;&emsp;operation|||false|integer(int32)||

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
	"data": { likes : 22 },  // 要返回最新点赞数量
	"errorMessage": "",
	"host": ""
}
```


## 加载评论列表

**接口地址**:`/api/v1/comment/load`


**请求方式**:`POST`


**请求数据类型**:`application/json`

**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"articleId": 0,  // 文章id   不能为空
	"index": 0, // 是否第一页 等于1代表第一页，热点评论时使用
	"minDate": "", // 当前页的最小评论时间，用于作为翻页条件  如果为空设置当前时间
	"size": 0 // 每页显示条数 如果为null或0 设置默认10条
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|CommentDto|CommentDto|
|&emsp;&emsp;articleId|||true|integer(int64)||
|&emsp;&emsp;index|||false|integer(int32)||
|&emsp;&emsp;minDate|||false|string(date-time)||
|&emsp;&emsp;size|||false|integer(int32)||


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
	"data": 评论集合,
	"errorMessage": "",
	"host": ""
}
```


## 保存评论信息

**接口地址**:`/api/v1/comment/save`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"articleId": 0, // 被评论的文章id   不能为空
	"content": ""  // 评论内容 不能为空 不能大于140个字符
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dto|dto|body|true|CommentSaveDto|CommentSaveDto|
|&emsp;&emsp;articleId|||true|integer(int64)||
|&emsp;&emsp;content|||true|string||


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

# 评论回复API

## 保存评论回复点赞

**接口地址**:`/api/v1/comment_repay/like`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
	"commentRepayId": "",  // 回复id 不能为空
	"operation": 0 // 点赞方式  0 点赞  1 取消赞
}
```

**请求参数**:

| 参数名称                   | 参数说明 | in   | 是否必须 | 数据类型            | schema              |
| -------------------------- | -------- | ---- | -------- | ------------------- | ------------------- |
| dto                        | dto      | body | true     | CommentRepayLikeDto | CommentRepayLikeDto |
| &emsp;&emsp;commentRepayId |          |      | true     | string              |                     |
| &emsp;&emsp;operation      |          |      | true     | integer(int32)      | 0点赞   1取消赞     |

**响应参数**:

| 参数名称     | 参数说明 | 类型           | schema         |
| ------------ | -------- | -------------- | -------------- |
| code         |          | integer(int32) | integer(int32) |
| data         |          | object         |                |
| errorMessage |          | string         |                |
| host         |          | string         |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": {},
	"errorMessage": "",
	"host": ""
}
```

## 加载评论回复列表

**接口地址**:`/api/v1/comment_repay/load`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
	"commentId": "", // 评论id  不能为空
	"minDate": "", // 当前页面最小回复时间 用于翻页   如果为空 设置当前时间
	"size": 0 // 每页条数 如果为null 或 0   设置默认:10
}
```

**请求参数**:

| 参数名称              | 参数说明 | in   | 是否必须 | 数据类型          | schema          |
| --------------------- | -------- | ---- | -------- | ----------------- | --------------- |
| dto                   | dto      | body | true     | CommentRepayDto   | CommentRepayDto |
| &emsp;&emsp;commentId |          |      | true     | string            |                 |
| &emsp;&emsp;minDate   |          |      | true     | string(date-time) |                 |
| &emsp;&emsp;size      |          |      | true     | integer(int32)    |                 |

**响应参数**:

| 参数名称     | 参数说明 | 类型           | schema         |
| ------------ | -------- | -------------- | -------------- |
| code         |          | integer(int32) | integer(int32) |
| data         |          | object         |                |
| errorMessage |          | string         |                |
| host         |          | string         |                |

**响应示例**:

```javascript
{
	"code": 0,
    "data":  评论回复集合,
	"errorMessage": "",
	"host": ""
}
```

## 保存评论回复

**接口地址**:`/api/v1/comment_repay/save`

**请求方式**:`POST`

**请求数据类型**:`application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
	"commentId": "", // 被回复的评论id
	"content": ""  // 回复内容
}
```

**请求参数**:

| 参数名称              | 参数说明 | in   | 是否必须 | 数据类型            | schema              |
| --------------------- | -------- | ---- | -------- | ------------------- | ------------------- |
| dto                   | dto      | body | true     | CommentRepaySaveDto | CommentRepaySaveDto |
| &emsp;&emsp;commentId |          |      | false    | string              |                     |
| &emsp;&emsp;content   |          |      | false    | string              |                     |

**响应参数**:

| 参数名称     | 参数说明 | 类型           | schema         |
| ------------ | -------- | -------------- | -------------- |
| code         |          | integer(int32) | integer(int32) |
| data         |          | object         |                |
| errorMessage |          | string         |                |
| host         |          | string         |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": {},
	"errorMessage": "",
	"host": ""
}
```

# 