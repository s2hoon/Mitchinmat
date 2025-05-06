package com.mitchinmat.global.security.oauth2.client;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

public class CustomAuthorizationCodeTokenResponseClient
	implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
	private static final String INVALID_TOKEN_RESPONSE_ERROR_CODE = "invalid_token_response";
	private Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> requestEntityConverter = new ClientAuthenticationMethodValidatingRequestEntityConverter(
		new OAuth2AuthorizationCodeGrantRequestEntityConverter());
	private RestOperations restOperations;

	public CustomAuthorizationCodeTokenResponseClient() {
		RestTemplate restTemplate = new RestTemplate(
			Arrays.asList(new FormHttpMessageConverter(), new OAuth2AccessTokenResponseHttpMessageConverter()));
		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
		this.restOperations = restTemplate;
	}

	public OAuth2AccessTokenResponse getTokenResponse(
		OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
		Assert.notNull(authorizationCodeGrantRequest, "authorizationCodeGrantRequest cannot be null");

		RequestEntity<?> request = (RequestEntity)this.requestEntityConverter.convert(authorizationCodeGrantRequest);
		ResponseEntity<OAuth2AccessTokenResponse> response = this.getResponse(request);

		OAuth2AccessTokenResponse tokenResponse = (OAuth2AccessTokenResponse)response.getBody();
		Assert.notNull(tokenResponse,
			"The authorization server responded to this Authorization Code grant request with an empty body; as such, it cannot be materialized into an OAuth2AccessTokenResponse instance. Please check the HTTP response code in your server logs for more details.");

		// refresh token 추출해서 additionalParameters에 추가
		OAuth2AccessToken accessToken = tokenResponse.getAccessToken();
		OAuth2RefreshToken refreshToken = tokenResponse.getRefreshToken();

		Map<String, Object> additionalParameters = new LinkedHashMap<>(tokenResponse.getAdditionalParameters());
		if (refreshToken != null) {
			additionalParameters.put("refresh_token", refreshToken.getTokenValue()); // 리프레시 토큰 추가
		}

		long expiresIn = accessToken.getExpiresAt().toEpochMilli() - System.currentTimeMillis();

		OAuth2AccessTokenResponse newTokenResponse = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
			.tokenType(accessToken.getTokenType())
			.expiresIn(expiresIn)
			.refreshToken(refreshToken.getTokenValue())
			.additionalParameters(additionalParameters)
			.build();
		return newTokenResponse;
	}

	private ResponseEntity<OAuth2AccessTokenResponse> getResponse(RequestEntity<?> request) {
		try {
			return this.restOperations.exchange(request, OAuth2AccessTokenResponse.class);
		} catch (RestClientException var4) {
			RestClientException ex = var4;
			OAuth2Error oauth2Error = new OAuth2Error("invalid_token_response",
				"An error occurred while attempting to retrieve the OAuth 2.0 Access Token Response: "
					+ ex.getMessage(), (String)null);
			throw new OAuth2AuthorizationException(oauth2Error, ex);
		}
	}

	public void setRequestEntityConverter(
		Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> requestEntityConverter) {
		Assert.notNull(requestEntityConverter, "requestEntityConverter cannot be null");
		this.requestEntityConverter = requestEntityConverter;
	}

	public void setRestOperations(RestOperations restOperations) {
		Assert.notNull(restOperations, "restOperations cannot be null");
		this.restOperations = restOperations;
	}
}
