package com.miniprogram.miniprogram;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.miniprogram.miniprogram.mapper")
public class MiniprogramApplication {
	public static void main(String[] args) {
		SpringApplication.run(MiniprogramApplication.class, args);
	}
}