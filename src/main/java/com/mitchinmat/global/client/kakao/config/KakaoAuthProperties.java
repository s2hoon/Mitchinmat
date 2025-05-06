package com.mitchinmat.global.client.kakao.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.kakao")
public class KakaoAuthProperties {

	private String clientId;
	private String clientSecret;
	private String redirectUri;

}
