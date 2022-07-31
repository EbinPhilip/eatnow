package com.ebin.eatnow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class EatnowApplication {

	public static void main(String[] args) {
		SpringApplication.run(EatnowApplication.class, args);
	}

}
