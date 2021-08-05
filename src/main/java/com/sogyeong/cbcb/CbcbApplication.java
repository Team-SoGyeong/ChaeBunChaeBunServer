package com.sogyeong.cbcb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class CbcbApplication {
	public static final String APPLICATION_LOCATIONS = "spring.config.import="
			+ "classpath:application.yml, "
			+ "/app/config/sogyeong-cbcb/real-application.yml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(CbcbApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);
		//test2
		//SpringApplication.run(CbcbApplication.class, args);
	}

}
