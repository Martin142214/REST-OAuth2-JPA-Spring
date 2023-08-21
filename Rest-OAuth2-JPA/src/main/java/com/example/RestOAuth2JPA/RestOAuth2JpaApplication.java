package com.example.RestOAuth2JPA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class }) 
public class RestOAuth2JpaApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(RestOAuth2JpaApplication.class, args);
	}

}
