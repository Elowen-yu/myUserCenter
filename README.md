# 用户中心 · 后端（User Center Backend）

> **这是我第一次写 Spring Boot 项目。** 从建库、写实体、调通接口，一路做到统一返回结构、全局异常处理和 Session 登录态，边学边写攒下来的一个完整练习。
> 学习过程中参考了编程导航（鱼皮）的「用户中心」项目教程，然后按自己的理解重新实现了一遍，代码里保留了不少学习痕迹，欢迎指点。

一个用户中心的后端服务：**注册 / 登录 / 查询用户 / 删除用户 / 注销**，登录态用 Session 保存，所有接口返回统一结构。

---

## 目录

- [功能一览](#功能一览)
- [技术栈](#技术栈)
- [目录结构](#目录结构)
- [快速开始](#快速开始)
- [接口说明](#接口说明)
- [统一返回结构与错误码](#统一返回结构与错误码)
- [设计要点与踩坑记录](#设计要点与踩坑记录)
- [相关文档](#相关文档)
- [已知问题与待优化](#已知问题与待优化)

---

## 功能一览

| 功能 | 说明 | 是否需要管理员 |
| --- | --- | --- |
| 注册 | 用户名 8~15 位；密码、确认密码各 ≥8 位且必须一致；用户名查重；密码 MD5 加密后入库 | 否 |
| 登录 | 校验账号密码，成功后把用户写入 Session 登录态 | 否 |
| 查询用户 | 按用户名**模糊查询**，返回 `VoUser` 列表（不含密码） | 是（`auth = 1`） |
| 删除用户 | 按 id **逻辑删除**（`delete_flag = 1`），数据仍保留在库中 | 是 |
| 注销 | 移除 Session 中的登录态 | 否 |

用户表字段：`id, nickname, username, password, avatar, gender, email, status(0默认/1封锁), auth(0默认/1管理员), delete_flag(0默认/1已删除)`

## 技术栈

| 分类 | 选型 | 版本 |
| --- | --- | --- |
| 语言 / 运行时 | Java | 17 |
| 框架 | Spring Boot（`spring-boot-starter-web`） | 3.4.5 |
| ORM | MyBatis-Plus（`mybatis-plus-spring-boot3-starter`） | 3.5.12 |
| ORM | MyBatis（`mybatis-spring-boot-starter`） | 3.0.4 |
| 数据库 | MySQL（`mysql-connector-j`） | — |
| 工具库 | Hutool（`hutool-all`，MD5、Bean 拷贝、字符串判空） | 5.8.37 |
| 代码简化 | Lombok | 1.18.38 |
| 构建 | Maven Wrapper（`mvnw` / `mvnw.cmd`） | — |

## 目录结构

```
user-center-backend-master
├── mvnw / mvnw.cmd / .mvn/            # Maven Wrapper，无需本机装 Maven
├── pom.xml
└── src
    ├── main
    │   ├── java/com/yufeng
    │   │   ├── UserCenterBackendMasterApplication.java   # 启动类
    │   │   ├── constant/Constant.java                    # 常量（Session key）
    │   │   ├── controller/UserController.java            # 接口层
    │   │   ├── service/UserService.java                  # 业务接口
    │   │   │   └── impl/UserServiceImpl.java             # 业务实现（核心逻辑都在这）
    │   │   ├── mapper/UserMapper.java                    # MyBatis-Plus BaseMapper
    │   │   ├── domain
    │   │   │   ├── po/User.java                          # 数据库实体
    │   │   │   ├── request/LoginUser.java                # 登录入参
    │   │   │   ├── request/RegisterUser.java             # 注册入参
    │   │   │   ├── response/BaseResponse.java            # 通用返回对象
    │   │   │   └── vo/VoUser.java                        # 脱敏后的用户视图对象
    │   │   ├── enums/ErrorCode.java                      # 错误码枚举
    │   │   ├── exception/BusinessException.java          # 自定义业务异常
    │   │   ├── exception/GlobalExceptionHandler.java     # 全局异常处理器
    │   │   └── util/ResultUtil.java                      # 返回结果封装工具
    │   └── resources
    │       ├── application.yml                           # 数据源 / Session / 前缀配置
    │       └── com/yufeng/mapper/UserMapper.xml          # resultMap 与字段列表
    └── test/java/com/yufeng                             # Spring Boot 自带的测试骨架
```

分层：`Controller → Service → Mapper`，异常由 `Service` 抛出、`GlobalExceptionHandler` 统一捕获并转成 `BaseResponse`。

## 快速开始

### 1. 环境要求

- JDK 17+
- MySQL 8.x
- Maven（可选，项目自带 `mvnw`，可以直接用）

### 2. 建库建表

数据库名默认是 `elowen`，表名 `user`（下面的建表语句是根据实体类 `User` 反推的，按需调整字段长度）：

```sql
CREATE DATABASE IF NOT EXISTS `elowen`
  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `elowen`;

CREATE TABLE IF NOT EXISTS `user` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'id',
  `nickname`    VARCHAR(64)  DEFAULT NULL COMMENT '昵称',
  `username`    VARCHAR(15)  NOT NULL COMMENT '用户名(8~15位)',
  `password`    VARCHAR(64)  NOT NULL COMMENT '密码(MD5, 32位)',
  `avatar`      VARCHAR(512) DEFAULT NULL COMMENT '头像',
  `gender`      TINYINT      DEFAULT NULL COMMENT '性别(1女2男)',
  `email`       VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
  `status`      TINYINT      NOT NULL DEFAULT 0 COMMENT '状态(0默认1封锁)',
  `auth`        TINYINT      NOT NULL DEFAULT 0 COMMENT '权限(0默认1管理员)',
  `delete_flag` TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除(0默认1已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_username` (`username`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '用户';
```

> 想用「管理员」接口（查询 / 删除用户），注册后把某条数据的 `auth` 手动改成 `1` 即可：
> `UPDATE user SET auth = 1 WHERE username = '你的用户名';`

### 3. 配置数据库账号密码

**数据库口令不写进代码库。** `src/main/resources/application.yml` 里用的是环境变量占位符：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/elowen?characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: ${MYSQL_USER:root}
    password: ${MYSQL_PASSWORD:}
```

所以启动前要先给 `MYSQL_USER` / `MYSQL_PASSWORD` 赋值：

- **PowerShell 里跑**

  ```powershell
  $env:MYSQL_USER = "root"
  $env:MYSQL_PASSWORD = "你的数据库密码"
  .\mvnw spring-boot:run
  ```

- **IDEA 里跑**：Run/Debug Configurations → Environment variables 里加上这两个变量，再运行 `UserCenterBackendMasterApplication`。

（`MYSQL_USER` 不设时默认 `root`；`MYSQL_PASSWORD` 不设就是空密码，连不上库。）

### 4. 启动

```powershell
.\mvnw spring-boot:run          # Windows
./mvnw spring-boot:run          # macOS / Linux
```

服务默认跑在 `8080` 端口，`application.yml` 里配了全局前缀 `server.servlet.context-path: /api`，所以接口地址是：

```
http://localhost:8080/api/user/...
```

## 接口说明

| 方法 | 路径 | 说明 | 入参 | 返回 |
| --- | --- | --- | --- | --- |
| POST | `/api/user/register` | 用户注册 | JSON：`username`、`password`、`checkPassword` | 新用户 `id` |
| POST | `/api/user/login` | 用户登录 | JSON：`username`、`password` | `VoUser`（不含密码），同时写入 Session |
| GET | `/api/user/search` | 按用户名模糊查询 | Query：`username` | `List<VoUser>`，查不到返回 `null` |
| DELETE | `/api/user/delete` | 按 id 逻辑删除 | Query：`id` | `true` / `false` |
| POST | `/api/user/logout` | 注销（清除登录态） | — | `0` |

**注册示例**

```bash
curl -X POST http://localhost:8080/api/user/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser01","password":"12345678","checkPassword":"12345678"}'
```

**登录示例**（登录态存在 Session 里，后续管理员接口要带上 Cookie）

```bash
curl -i -X POST http://localhost:8080/api/user/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser01","password":"12345678"}'
```

**查询示例**

```bash
curl -X GET "http://localhost:8080/api/user/search?username=test" \
  -H "Cookie: JSESSIONID=登录时返回的会话ID"
```

## 统一返回结构与错误码

所有接口都返回同一个结构（`BaseResponse<T>`），前端只要认这一套：

```json
{
  "code": 0,
  "message": "success",
  "description": " ",
  "data": 1
}
```

- `code`：业务状态码，`0` 表示成功，其余见下表
- `message`：状态信息
- `description`：更详细的描述（失败原因）
- `data`：成功时返回的数据

| code | 常量 | 含义 |
| --- | --- | --- |
| 0 | `SUCCESS` | 成功 |
| 4000 | `PARAMS_ERROR` | 请求参数错误 |
| 4001 | `NULL_ERROR` | 请求参数为空 |
| 40100 | `NO_LOGIN_ERROR` | 未登录 |
| 40101 | `NO_AUTH_ERROR` | 无权限 |
| 500 | `SYSTEM_ERROR` | 系统异常 |

错误码集中定义在 `enums/ErrorCode.java`，改提示语只需要改这一个枚举类。

## 设计要点与踩坑记录

### 1. 异常处理链路（我最想记下来的一段）

```
客户端请求 → Controller → Service → Mapper
异常发生   → 抛出自定义 BusinessException
           → @RestControllerAdvice 全局异常处理器捕获
           → 转成通用返回对象 BaseResponse
           → 返回给前端
```

- Controller 里**不再写 try-catch**，业务层直接 `throw new BusinessException(ErrorCode.XXX, "说明")`；
- `GlobalExceptionHandler` 里兜底处理 `RuntimeException`，避免把堆栈直接暴露给前端；
- `BaseResponse` + `ResultUtil` + `ErrorCode` 三件套让「成功/失败」的写法固定下来，业务代码可读性明显变好。

### 2. Session 登录态与鉴权

- 登录成功后 `request.getSession().setAttribute("userLoginState", user)`；
- 查询 / 删除接口先从 Session 取当前用户，取不到说明没登录，再判断 `auth == 1` 决定是否有权限；
- Session 有效期在 `application.yml` 配成 `86400` 秒（一天）；
- 注销就是 `removeAttribute`，把登录态删掉。

### 3. 逻辑删除（MyBatis-Plus）

- 实体字段加 `@TableLogic`，并在 `application.yml` 里配置全局逻辑删除值：`delete_flag`，删除=1、未删除=0；
- 之后所有查询自动带上 `delete_flag = 0`，`removeById` 也变成 `UPDATE ... SET delete_flag = 1`。

### 4. 踩过的坑

| 问题 | 原因 | 解决 |
| --- | --- | --- |
| 登录接口返回 `400 Bad Request` | 给 `username` 和 `password` 各加了一个 `@RequestBody`，而 `@RequestBody` 只能修饰**整个请求体**，Spring 解析不了 | 用一个入参对象（`LoginUser`）接收整个 JSON |
| 接口路径写不对 | 没注意全局前缀 | `server.servlet.context-path: /api`，实际路径是 `/api/user/...` |
| 密码不能返回给前端 | `User` 实体带 `password` | 新增 `VoUser`（不含密码），用 `BeanUtil.copyProperties` 转换 |

## 相关文档

| 文件 | 内容 |
| --- | --- |
| [myUserCenter.md](./myUserCenter.md) | 需求分析、数据库设计、详细设计、后端优化（Session / 异常）全过程笔记 |
| [API_Documentation.md](./API_Documentation.md) | 接口文档：参数、响应、示例 |
| [README_API.md](./README_API.md) | 上面这份文档的使用说明（Postman / Swagger / 测试脚本怎么用） |
| [swagger-api-doc.yaml](./swagger-api-doc.yaml) | OpenAPI 3.0 规范，可导入 Swagger UI |
| [UserCenter_API.postman_collection.json](./UserCenter_API.postman_collection.json) | Postman 测试集合，导入即用 |
| [test_api.py](./test_api.py) | 简单的 Python 接口测试脚本（`pip install requests` 后 `python test_api.py`） |
| [Frontend_Requirements.md](./Frontend_Requirements.md) | 前端需求说明 |
| [project-config.json](./project-config.json) | 前端页面 / 路由配置草稿 |

## 已知问题与待优化

第一次写，问题不少，先如实记下：

- **密码用 MD5 加密且没有加盐**，只能算练习级别；准备换成 BCrypt + 随机盐。
- 未登录时部分接口抛的是 `NULL_ERROR`（4001）而不是 `NO_LOGIN_ERROR`（40100），语义不准，待统一。
- 删除一个**已经被删过**的 id，返回仍是 `success`，只是 `data` 为 `false`（`removeById` 的行为）。
- Session 存在单机内存里，多实例部署会有登录态不一致的问题；后续想学 Spring Session + Redis。
- 查询接口没有分页，用户量大了会有问题。
- 表结构缺少 `create_time` / `update_time` 字段。
- 还没有用 `@Slf4j` 打日志，排查问题全靠调试。
- 单元测试只有 Spring Boot 自带的骨架，业务逻辑没有测试覆盖。
- 还有教程里提到的扩展功能没做完。

---

如果这份代码帮你少踩一个坑，那它就值了。欢迎提 issue 交流 🛠️
