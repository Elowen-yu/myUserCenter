# 用户中心API文档使用说明

## 文档文件说明

本项目提供了多种格式的API文档，方便不同场景下的使用：

### 1. API_Documentation.md
- **格式**: Markdown
- **用途**: 详细的API接口文档，包含完整的接口说明、参数、响应格式和示例
- **适用场景**: 开发人员查阅、团队协作、文档归档

### 2. swagger-api-doc.yaml
- **格式**: OpenAPI 3.0 (Swagger)
- **用途**: 标准化的API规范文档
- **适用场景**: 
  - 导入到Swagger UI进行可视化展示
  - 代码生成工具使用
  - API网关配置
  - 自动化测试

### 3. UserCenter_API.postman_collection.json
- **格式**: Postman Collection
- **用途**: Postman测试集合
- **适用场景**: 
  - 接口测试
  - 开发调试
  - 团队协作测试

### 4. test_api.py
- **格式**: Python脚本
- **用途**: 自动化API测试脚本
- **适用场景**: 
  - 自动化测试
  - CI/CD集成
  - 接口验证

## 使用方法

### 查看API文档

1. **Markdown文档**: 直接打开 `API_Documentation.md` 文件查看
2. **Swagger文档**: 
   - 访问 [Swagger Editor](https://editor.swagger.io/)
   - 将 `swagger-api-doc.yaml` 内容复制粘贴到编辑器中
   - 或者使用本地Swagger UI服务

### 使用Postman测试

1. 打开Postman
2. 点击 "Import" 按钮
3. 选择 `UserCenter_API.postman_collection.json` 文件
4. 导入后可以看到所有接口的测试用例
5. 修改环境变量 `baseUrl` 为你的服务器地址
6. 逐个执行测试用例

### 运行自动化测试

1. 确保Python环境已安装
2. 安装依赖：`pip install requests`
3. 运行测试脚本：`python test_api.py`

## 接口概览

| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 用户注册 | POST | `/user/register` | 新用户注册 |
| 用户登录 | POST | `/user/login` | 用户登录认证 |
| 用户查询 | GET | `/user/search` | 根据用户名查询用户 |
| 用户删除 | DELETE | `/user/delete` | 删除指定用户 |
| 用户登出 | POST | `/user/logout` | 用户登出 |

## 响应格式

所有接口都使用统一的响应格式：

```json
{
  "code": 0,
  "message": "success",
  "description": " ",
  "data": {}
}
```

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 0 | 成功 |
| 4000 | 请求参数错误 |
| 4001 | 请求参数为空 |
| 40100 | 未登录 |
| 40101 | 无权限 |
| 500 | 系统异常 |

## 开发建议

1. **安全性**: 
   - 生产环境建议使用HTTPS
   - 密码传输需要加密
   - 实现适当的权限控制

2. **性能优化**:
   - 添加接口缓存
   - 实现分页查询
   - 优化数据库查询

3. **功能扩展**:
   - 添加用户信息更新接口
   - 实现密码重置功能
   - 添加用户头像上传

4. **监控和日志**:
   - 添加接口调用日志
   - 实现性能监控
   - 错误追踪和告警

## 技术支持

如有问题或建议，请联系开发团队。 