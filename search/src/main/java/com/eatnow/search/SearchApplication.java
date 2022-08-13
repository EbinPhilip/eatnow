package com.eatnow.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.eatnow.search.repositories.elastic")
@EnableFeignClients
@OpenAPIDefinition(info = @Info(title = "Search", version = "1.0", description = "APIs for text searching"))
@SecurityScheme(name = "user token", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class SearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchApplication.class, args);
	}

}
