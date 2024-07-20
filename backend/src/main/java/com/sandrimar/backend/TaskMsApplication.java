package com.sandrimar.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TaskMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskMsApplication.class, args);
	}

}
