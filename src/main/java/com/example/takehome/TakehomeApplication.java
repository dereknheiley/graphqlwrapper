package com.example.takehome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling 
@ComponentScan(basePackages = "com.example.takehome")
@SpringBootApplication
public class TakehomeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TakehomeApplication.class, args);
	}
}
