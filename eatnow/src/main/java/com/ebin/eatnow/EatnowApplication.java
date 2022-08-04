package com.ebin.eatnow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(exclude= {UserDetailsServiceAutoConfiguration.class})
@EnableMongoRepositories(basePackages = "com.ebin.eatnow.repositories.mongoDao")
@EnableJpaRepositories(basePackages =  "com.ebin.eatnow.repositories.jpaDao")
public class EatnowApplication {

	public static void main(String[] args) {
		SpringApplication.run(EatnowApplication.class, args);
	}

}
