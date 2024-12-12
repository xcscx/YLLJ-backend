package com.itegg.yllj_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.itegg.yllj_backend.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
public class YlljBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(YlljBackendApplication.class, args);
    }

}
