package com.springmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebApplication {

	private static Class<WebApplication> applicationClass = WebApplication.class;

	public static void main(String[] args) throws Exception {
		// System.setProperty("spring.devtools.restart.enabled", "false");
		SpringApplication.run(applicationClass, args);
	}

}