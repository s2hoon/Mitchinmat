package com.mitchinmat.global.config;

import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import jakarta.validation.constraints.NotBlank;

@Configuration
@SecurityScheme(
	name = "bearerAuth",
	type = SecuritySchemeType.HTTP,
	scheme = "bearer",
	bearerFormat = "JWT"
)
public class SwaggerConfig {
	static {
		// SpringDocUtils to enable HTML tags support in descriptions
		SpringDocUtils.getConfig().addAnnotationsToIgnore(NotBlank.class);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.info(new Info()
				.title("믿친맛 API 명세서")
				.version("1.0")
				.description("믿친맛 API 명세서 입니다"))
			.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
	}
}
