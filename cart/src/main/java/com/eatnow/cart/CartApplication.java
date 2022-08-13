package com.eatnow.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@EnableFeignClients
@EnableMongoRepositories(basePackages = "com.eatnow.cart.repositories.mongoDao")
@OpenAPIDefinition(info = @Info(title = "Cart API", version = "1.0", description = "APIs for cart management"), security = {
	@SecurityRequirement(name = "user token") })
@SecurityScheme(name = "user token", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
@SpringBootApplication(exclude= {UserDetailsServiceAutoConfiguration.class})
public class CartApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartApplication.class, args);
	}
}
