package com.baldwin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.baldwin.dao")
@SpringBootApplication
public class BaldwinApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaldwinApplication.class, args);
    }
}
