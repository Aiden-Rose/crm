package com.xxx.crm;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xxx.crm.dao")
public class Starter {
    public static void main(String[] args) {
        SpringApplication.run(Starter.class);
    }
}

