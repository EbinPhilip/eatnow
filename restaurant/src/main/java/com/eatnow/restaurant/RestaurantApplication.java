package com.eatnow.restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication(exclude= {UserDetailsServiceAutoConfiguration.class})
@EnableMongoRepositories(basePackages = "com.eatnow.restaurant.repositories.mongoDao")
@EnableElasticsearchRepositories(basePackages = "com.eatnow.restaurant.repositories.elasticsearch")
@OpenAPIDefinition(info = @Info(title = "Restaurant", version = "1.0", description = "Restaurant management APIs"), security = {
	@SecurityRequirement(name = "restaurant token") })
@SecurityScheme(name = "restaurant token", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class RestaurantApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantApplication.class, args);
	}

}
