package com.mitchinmat.global.client.kakao.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoOAuthResponse(
	String tokenType,
	String accessToken,
	Long expiresIn,
	String refreshToken,
	Long refreshTokenExpiresIn
) {
}
