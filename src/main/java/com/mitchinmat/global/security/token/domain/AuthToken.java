package com.mitchinmat.global.security.token.domain;

import org.springframework.security.oauth2.core.OAuth2AccessToken;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthToken {
	private Long userId;
	private String token;
	private Long expiresIn;
	private AuthTokenType type;

	@Builder
	public AuthToken(Long userId, String token, Long expiresIn, AuthTokenType type) {
		this.userId = userId;
		this.token = token;
		this.expiresIn = expiresIn;
		this.type = type;
	}

	public static AuthToken fromOAuth2AccessToken(Long userId, OAuth2AccessToken oAuth2AccessToken) {
		long expiresIn = oAuth2AccessToken.getExpiresAt().toEpochMilli() - System.currentTimeMillis();
		return AuthToken.builder()
			.userId(userId)
			.token(oAuth2AccessToken.getTokenValue())
			.expiresIn(expiresIn)
			.type(AuthTokenType.OAUTH2_ACCESS)
			.build();
	}
}
