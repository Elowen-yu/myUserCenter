## 自己复现用户中心

### 1.需求分析

1.注册功能 data=User

​	1.三个任一都不许为空

​	2.验证账号的长度大于8小于15

​	3.验证密码和确定密码的长度大于8

​	4.验证密码和确定密码是否一致

​	5.将密码加密

​	6.将数据传入数据库

2.登录功能

​	1.账号密码不能为空

​	2.验证账号的长度大于8小于15

​	3.验证密码的长度大于8

​	4.查看数据库是否有相关的数据

​		密码错误

​		密码正确，登录

​	5.记录session，便于用户对其他页面的使用

​		session错误，直接跳到登录页面

3.用户管理功能

​	1.根据用户名查询用户

​	2.删除用户(逻辑删除)

### 2.技术选型

1.后端初始化

​	springboot，lombok，web，mysql, mybatis,mybatis-plus

2.mybatisPlus

官网

mybatis-X生成mapper，mapper.xml，User,sevice

测试

3.hutool工具包

### 3.数据库设计

id,昵称nickname，用户名username,密码password,头像avatar,性别gender,邮箱email,状态status(0默认1封锁)，权限auth(0默认1管理员)，逻辑删除delete_Flag（0默认1已删除）

（逻辑设计 功能：接口设计，逻辑，实现）



### 4.详细设计

1.注册功能逻辑

​	先创建针对注册功能的数据：账号，密码，确定密码

​	1.三个任一都不许为空

​	2.验证账号的长度大于8小于15

​	3.验证密码和确定密码的长度大于8

​	4.验证密码和确定密码是否一致

​	5.补充：用户名是否重复

​	5.将密码加密

​	6.将数据传入数据库

2.登录功能逻辑

​	1.账号密码不能为空

​	2.验证账号的长度大于8小于15

​	3.验证密码的长度大于8

​	4.查看数据库是否有相关的数据

​		密码错误

​		密码正确，登录

3.注册接口

​	@PostMapping

​	1.逻辑调用

​	2.返回id

4.登录接口

​	返回voUser,保护密码

5.测试接口

postman

​	1.登录接口400 error:BadRequest 

​		原因：你为 username和 password分别加了@RequestBody但`@RequestBody` 只能用于 **整个请求体**，不能拆分到多个参数。这会导致 Spring 无法解析请求，直接返回 `400`。

​	设置前缀

```
server:
  servlet:
    context-path: /api
```

6.根据用户名查询用户逻辑

​	1.查询 mp：模糊查询

​	2.返回的内容List/<VoUser/>

​	id,昵称nickname，用户名username,,头像avatar,性别gender,邮箱email,状态status(0默认1封锁)，权限auth(0默认1管理员)，逻辑删除delete_Flag（0默认1已删除）

7.根据id删除用户(逻辑删除)

​	1.配置全局逻辑删除属性

​	2.在实体类中使用 `@TableLogic` 注解

​	bug: 如果要删除的用户已经被删除了，message还是success，只是data是false。不过在业务中id会通过图形化页面传递，应该不会有传递已被删除的id的情况

8.查询接口

​	@GetMapping

9.删除接口

​	@DeleteMapping

优化session

10.注销接口

​	删除ssesion的登录态 removeAttribute

​	@PostMapping

### 后端优化

#### 1.session

1.登录接口保存session

   1.什么是session：

​	1.[spring-session](https://spring.io/projects/spring-session)

​	2.[【知识】深入理解COOKIE&SESSION的原理和区别-腾讯云开发者社区-腾讯云](https://cloud.tencent.com/developer/article/2002609)

​	3.[Session执行原理、Session的常用方法及Session对象的创建与获取 - 动力节点](https://www.bjpowernode.com/tutorial_session/1059.html)

​	 session是服务器中用于存储用户信息的。

   2.步骤

​	1.登录逻辑中，设置请求中的session属性为已登录。key=字符串 value=user

2.用户管理接口通过session鉴权

​	从session的属性中拿到当前登录的user，根据权限auth鉴权

3.设置session失效时间

​	spring:session:timeout:86400(s 一天)

4.测试

​	每个接口都要检查request==null

​	postman



#### 2.异常

##### 请求流程：

客户端请求 → 控制器 → 服务层 → 数据层

##### 异常处理流程：
异常发生 → **自定义异常**抛出 → **全局异常处理器**捕获 → 转换为**通用返回对象** → 返回给前端

##### 1.通用返回对象

BaseResponse

1.意义：前端始终收到固定结构的JSON

​	前后端分离，告诉前端该请求在业务层面是成功还是失败

2.属性：业务状态码，状态信息，返回数据，详细描述

静态方法：成功（返回数据），失败（返回错误状态码和信息）

3.

##### 2.错误状态代码集中定义

1.意义：修改错误提示只需改ErrorCode枚举类

​	   可维护性

2.枚举类

枚举跟普通类一样可以用自己的变量、方法和构造函数，构造函数只能使用 private 访问修饰符，所以外部无法调用。

枚举既可以包含具体方法，也可以包含抽象方法。 如果枚举类具有抽象方法，则枚举类的每个实例都必须实现它。

**枚举元素本身就相当于枚举类的一个对象，调用枚举类的构造方法**

属性：状态码，状态信息，详细描述

3.状态内容

success 0  SUCCESS

请求参数错误 4000 PARAMS_ERROR

请求参数为空 4001 NULL_ERROR

未登录 40100 NO_LOGIN_ERROR

无权限 40101NO_AUTH_ERROR

系统异常 500 SYSTEM_ERROR



定义完第一个和第二个，再用ResultUtil类封装success方法和error方法，让业务代码的可读性更强。

然后就可以开始更改业务层代码的成功部分。



##### 3.自定义异常

1.意义：在系统自带异常基础上完善业务异常的规则

​	  可读性

2.格式

​	1.继承RuntimeException

```java
//RuntimeException的构造函数
public RuntimeException(String message, Throwable cause) {
    super(message, cause);
}
```

​	2.构造函数

​		添加通用返回对象的属性：状态码、详细描述。以便更清晰地描述异常情况。

​		状态信息父类已有，data是成功才返回的。

##### 4.全局异常处理器

1.意义：Controller不再有try-catch，业务层直接throw异常。当**throw异常时**，*全局异常处理器*会捕捉并调用处理方法，**返回通用返回对象**。

​	  代码整洁

2.格式：

​	1.类名，方法名，方法传参

​	2.注解

​	3.返回值类型

````JAVA
@RestControllerAdvice
public class GlobalExceptionHandler(){
 @ExceptionHandler(xxException.class)
public 通用返回对象 handlexxException(xxException e){
    return ResultUtil.error(...);
}
    
}
````



5.最后，修改业务层代码和控制层代码。

将控制层的返回值都改为通用返回对象BaseResponse<T>，异常部分抛异常

业务层的返回值不变，异常部分抛异常



6.测试



### 待优化部分

1.鱼皮说的拓展部分

2.没有用@slf4j
3.数据库的创建时间，更新时间
