package com.mitchinmat.global.security.oauth2.model;

import java.util.Arrays;

public enum OAuth2Provider {
	KAKAO;

	public static OAuth2Provider convert(String registrationId) {
		return Arrays.stream(OAuth2Provider.values())
			.filter(provider -> provider.toString().equals(registrationId.toUpperCase()))
			.findAny()
			.orElseThrow(IllegalArgumentException::new);
	}
}
