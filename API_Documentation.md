# 用户中心后端 API 接口文档

## 基础信息

- **基础URL**: `http://localhost:8080`
- **API版本**: v1.0
- **内容类型**: `application/json`

## 通用响应格式

所有接口都使用统一的响应格式：

```json
{
  "code": 0,
  "message": "success",
  "description": " ",
  "data": {}
}
```

### 响应字段说明

| 字段 | 类型 | 说明 |
|------|------|------|
| code | Integer | 状态码，0表示成功 |
| message | String | 状态消息 |
| description | String | 详细描述 |
| data | Object | 响应数据 |

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 0 | 成功 |
| 4000 | 请求参数错误 |
| 4001 | 请求参数为空 |
| 40100 | 未登录 |
| 40101 | 无权限 |
| 500 | 系统异常 |

## 接口列表

### 1. 用户注册

**接口地址**: `POST /user/register`

**接口描述**: 用户注册接口

**请求参数**:

```json
{
  "username": "string",
  "password": "string", 
  "checkPassword": "string"
}
```

**参数说明**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |
| checkPassword | String | 是 | 确认密码 |

**响应示例**:

```json
{
  "code": 0,
  "message": "success",
  "description": " ",
  "data": 123456
}
```

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| data | Long | 注册成功的用户ID |

---

### 2. 用户登录

**接口地址**: `POST /user/login`

**接口描述**: 用户登录接口

**请求参数**:

```json
{
  "username": "string",
  "password": "string"
}
```

**参数说明**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |

**响应示例**:

```json
{
  "code": 0,
  "message": "success",
  "description": " ",
  "data": {
    "id": 123456,
    "nickname": "用户昵称",
    "username": "testuser",
    "avatar": "头像URL",
    "gender": 1,
    "email": "test@example.com",
    "status": 0,
    "auth": 0,
    "deleteFlag": 0
  }
}
```

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| data.id | Long | 用户ID |
| data.nickname | String | 用户昵称 |
| data.username | String | 用户名 |
| data.avatar | String | 头像URL |
| data.gender | Integer | 性别(1女2男) |
| data.email | String | 邮箱 |
| data.status | Integer | 状态(0默认1封锁) |
| data.auth | Integer | 权限(0默认1管理员) |
| data.deleteFlag | Integer | 逻辑删除标识(0默认1已删除) |

---

### 3. 用户查询

**接口地址**: `GET /user/search`

**接口描述**: 根据用户名查询用户信息

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | String | 是 | 用户名(支持模糊查询) |

**请求示例**: `GET /user/search?username=test`

**响应示例**:

```json
{
  "code": 0,
  "message": "success",
  "description": " ",
  "data": [
    {
      "id": 123456,
      "nickname": "用户昵称",
      "username": "testuser",
      "avatar": "头像URL",
      "gender": 1,
      "email": "test@example.com",
      "status": 0,
      "auth": 0,
      "deleteFlag": 0
    }
  ]
}
```

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| data | Array | 用户信息列表 |
| data[].id | Long | 用户ID |
| data[].nickname | String | 用户昵称 |
| data[].username | String | 用户名 |
| data[].avatar | String | 头像URL |
| data[].gender | Integer | 性别(1女2男) |
| data[].email | String | 邮箱 |
| data[].status | Integer | 状态(0默认1封锁) |
| data[].auth | Integer | 权限(0默认1管理员) |
| data[].deleteFlag | Integer | 逻辑删除标识(0默认1已删除) |

---

### 4. 用户删除

**接口地址**: `DELETE /user/delete`

**接口描述**: 根据用户ID删除用户

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 用户ID |

**请求示例**: `DELETE /user/delete?id=123456`

**响应示例**:

```json
{
  "code": 0,
  "message": "success",
  "description": " ",
  "data": true
}
```

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| data | Boolean | 删除结果，true表示成功 |

---

### 5. 用户登出

**接口地址**: `POST /user/logout`

**接口描述**: 用户登出接口

**请求参数**: 无

**响应示例**:

```json
{
  "code": 0,
  "message": "success",
  "description": " ",
  "data": 123456
}
```

**响应字段说明**:

| 字段 | 类型 | 说明 |
|------|------|------|
| data | Integer | 登出用户的ID |

---

## 错误响应示例

### 参数错误

```json
{
  "code": 4000,
  "message": "请求参数错误",
  "description": "用户名不能为空",
  "data": null
}
```

### 未登录

```json
{
  "code": 40100,
  "message": "未登录",
  "description": "请先登录",
  "data": null
}
```

### 系统异常

```json
{
  "code": 500,
  "message": "系统异常",
  "description": "服务器内部错误",
  "data": null
}
```

## 注意事项

1. 所有需要登录的接口都需要在请求头中携带有效的会话信息
2. 密码在传输过程中建议使用HTTPS加密
3. 用户名查询支持模糊匹配
4. 删除操作为逻辑删除，不会物理删除数据
5. 性别字段：1表示女性，2表示男性
6. 状态字段：0表示正常，1表示封锁
7. 权限字段：0表示普通用户，1表示管理员 