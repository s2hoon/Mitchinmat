package com.mitchinmat.global.security.oauth2.service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.mitchinmat.domain.user.application.UserRegistrationService;
import com.mitchinmat.domain.user.domain.User;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;
import com.mitchinmat.global.security.oauth2.model.OAuth2UserInfoFactory;
import com.mitchinmat.global.security.oauth2.model.userinfo.OAuth2UserInfo;
import com.mitchinmat.global.security.token.dao.RedisTokenRepository;
import com.mitchinmat.global.security.token.domain.AuthToken;
import com.mitchinmat.global.security.token.domain.AuthTokenType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRegistrationService userRegistrationService;
	private final RedisTokenRepository redisTokenRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId,
			oAuth2User.getAttributes());

		User user = userRegistrationService.registerUser(oAuth2UserInfo);
		AuthToken oAuth2AccessToken = AuthToken.fromOAuth2AccessToken(user.getId(), userRequest.getAccessToken());
		redisTokenRepository.saveToken(oAuth2AccessToken);

		AuthToken oAuth2RefreshToken = extractRefreshToken(userRequest, user.getId());
		if (oAuth2RefreshToken != null) {
			redisTokenRepository.saveToken(oAuth2RefreshToken);
		}

		return buildCustomOAuth2User(user, oAuth2UserInfo, userRequest);
	}

	private AuthToken extractRefreshToken(OAuth2UserRequest userRequest, Long userId) {
		Map<String, Object> additionalParameters = userRequest.getAdditionalParameters();
		String refreshTokenValue = (String)additionalParameters.get("refresh_token");
		Long refreshTokenExpiresIn = ((Number)additionalParameters.get("refresh_token_expires_in")).longValue();

		if (refreshTokenValue != null) {
			return AuthToken.builder()
				.userId(userId)
				.token(refreshTokenValue)
				.expiresIn(refreshTokenExpiresIn)
				.type(AuthTokenType.OAUTH2_REFRESH)
				.build();
		}
		return null;
	}

	private CustomOAuth2User buildCustomOAuth2User(User user, OAuth2UserInfo oAuth2UserInfo,
		OAuth2UserRequest userRequest) {
		Set<SimpleGrantedAuthority> authorities = Collections.singleton(
			new SimpleGrantedAuthority(user.getRole().getAuthority()));
		return CustomOAuth2User.builder()
			.authorities(authorities)
			.attributes(oAuth2UserInfo.getExtraData())
			.nameAttributeKey(userRequest.getClientRegistration()
				.getProviderDetails()
				.getUserInfoEndpoint()
				.getUserNameAttributeName())
			.userId(user.getId())
			.oauth2Id(user.getOauth2Id())
			.build();
	}

}
