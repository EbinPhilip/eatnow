package com.eatnow.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class })
@EnableJpaRepositories(basePackages = "com.eatnow.user.repositories.jpaDao")
@OpenAPIDefinition(info = @Info(title = "User", version = "1.0", description = "User management APIs"), security = {
		@SecurityRequirement(name = "user token") })
@SecurityScheme(name = "user token", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

}
