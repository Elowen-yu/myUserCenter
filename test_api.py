#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
用户中心API接口测试脚本
"""

import requests
import json
import time

class UserCenterAPITest:
    def __init__(self, base_url="http://localhost:8080"):
        self.base_url = base_url
        self.session = requests.Session()
        self.test_user = {
            "username": f"testuser_{int(time.time())}",
            "password": "123456",
            "checkPassword": "123456"
        }
        
    def print_response(self, response, title):
        """打印响应结果"""
        print(f"\n{'='*50}")
        print(f"{title}")
        print(f"{'='*50}")
        print(f"状态码: {response.status_code}")
        print(f"响应内容: {json.dumps(response.json(), ensure_ascii=False, indent=2)}")
        
    def test_register(self):
        """测试用户注册"""
        url = f"{self.base_url}/user/register"
        data = self.test_user
        
        try:
            response = self.session.post(url, json=data)
            self.print_response(response, "用户注册测试")
            return response.json().get('data')  # 返回用户ID
        except Exception as e:
            print(f"注册测试失败: {e}")
            return None
            
    def test_login(self):
        """测试用户登录"""
        url = f"{self.base_url}/user/login"
        data = {
            "username": self.test_user["username"],
            "password": self.test_user["password"]
        }
        
        try:
            response = self.session.post(url, json=data)
            self.print_response(response, "用户登录测试")
            return response.json().get('data')  # 返回用户信息
        except Exception as e:
            print(f"登录测试失败: {e}")
            return None
            
    def test_search(self):
        """测试用户查询"""
        url = f"{self.base_url}/user/search"
        params = {"username": self.test_user["username"]}
        
        try:
            response = self.session.get(url, params=params)
            self.print_response(response, "用户查询测试")
            return response.json().get('data')
        except Exception as e:
            print(f"查询测试失败: {e}")
            return None
            
    def test_delete(self, user_id):
        """测试用户删除"""
        url = f"{self.base_url}/user/delete"
        params = {"id": user_id}
        
        try:
            response = self.session.delete(url, params=params)
            self.print_response(response, "用户删除测试")
            return response.json().get('data')
        except Exception as e:
            print(f"删除测试失败: {e}")
            return None
            
    def test_logout(self):
        """测试用户登出"""
        url = f"{self.base_url}/user/logout"
        
        try:
            response = self.session.post(url)
            self.print_response(response, "用户登出测试")
            return response.json().get('data')
        except Exception as e:
            print(f"登出测试失败: {e}")
            return None
            
    def run_all_tests(self):
        """运行所有测试"""
        print("开始用户中心API接口测试...")
        print(f"测试用户: {self.test_user['username']}")
        
        # 1. 测试注册
        user_id = self.test_register()
        if not user_id:
            print("注册失败，停止测试")
            return
            
        # 2. 测试登录
        user_info = self.test_login()
        if not user_info:
            print("登录失败，停止测试")
            return
            
        # 3. 测试查询
        self.test_search()
        
        # 4. 测试删除
        self.test_delete(user_id)
        
        # 5. 测试登出
        self.test_logout()
        
        print("\n所有测试完成！")

def main():
    """主函数"""
    # 创建测试实例
    tester = UserCenterAPITest()
    
    # 运行所有测试
    tester.run_all_tests()

if __name__ == "__main__":
    main() 