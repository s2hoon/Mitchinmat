package com.mitchinmat.global.client.kakao.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoPlaceRequest(
	@NotNull(message = "Query cannot be blank")
	String query,

	@Min(value = 1, message = "Page number must be at least 1")
	@Max(value = 45, message = "Page number cannot be more than 100")
	Integer page,

	@Min(value = 1, message = "Size must be at least 1")
	@Max(value = 15, message = "Size cannot be more than 50")
	Integer size,

	@NotNull(message = "Longitude (x) cannot be null")
	@DecimalMin(value = "-180.0", inclusive = true, message = "Longitude (x) must be between -180 and 180")
	@DecimalMax(value = "180.0", inclusive = true, message = "Longitude (x) must be between -180 and 180")
	Double x,

	@NotNull(message = "Latitude (y) cannot be null")
	@DecimalMin(value = "-90.0", inclusive = true, message = "Latitude (y) must be between -90 and 90")
	@DecimalMax(value = "90.0", inclusive = true, message = "Latitude (y) must be between -90 and 90")
	Double y
) {
}
