package com.decoo.psa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication(scanBasePackages = {"com.decoo"})
@MapperScan("com.decoo.psa.dao")
@ServletComponentScan
public class Main {
    public static void main(String[] args){
        SpringApplication.run(Main.class, args);
    }
}
