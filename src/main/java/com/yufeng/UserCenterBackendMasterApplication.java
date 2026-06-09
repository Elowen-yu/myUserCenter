package com.yufeng;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yufeng.mapper")
public class UserCenterBackendMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterBackendMasterApplication.class, args);
    }

}
