package com.sogyeong.cbcb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CbcbApplication {
	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "classpath:application.yml,"
			+ "/Users/yooyounglee/Desktop/app/config/sogyeong-cbcb/real-application.yml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(CbcbApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);
		//SpringApplication.run(CbcbApplication.class, args);
	}

}
