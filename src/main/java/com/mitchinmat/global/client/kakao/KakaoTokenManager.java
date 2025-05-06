package com.mitchinmat.global.client.kakao;

import static com.mitchinmat.global.error.ErrorCode.*;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.mitchinmat.global.client.kakao.dto.response.KakaoOAuthResponse;
import com.mitchinmat.global.error.exception.MitchinmatException;
import com.mitchinmat.global.security.token.dao.RedisTokenRepository;
import com.mitchinmat.global.security.token.domain.AuthToken;
import com.mitchinmat.global.security.token.domain.AuthTokenType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KakaoTokenManager {
	private final RedisTokenRepository redisTokenRepository;
	private final KakaoClient kakaoClient;

	public String getOrReissueAccessToken(Long userId) {
		Optional<String> kakaoAccessToken = redisTokenRepository.findToken(AuthTokenType.OAUTH2_ACCESS, userId);
		if (kakaoAccessToken.isPresent() && kakaoClient.isAccessTokenValid(kakaoAccessToken.get())) {
			return kakaoAccessToken.get();
		}

		String storedKakaoRefreshToken = redisTokenRepository.findToken(AuthTokenType.OAUTH2_REFRESH, userId)
			.orElseThrow(() -> new MitchinmatException(KAKAO_REFRESH_TOKEN_EXPIRED));
		KakaoOAuthResponse tokenResponse = kakaoClient.reissueToken(storedKakaoRefreshToken);

		AuthToken newAccessToken = AuthToken.builder()
			.userId(userId)
			.token(tokenResponse.accessToken())
			.expiresIn(tokenResponse.expiresIn())
			.type(AuthTokenType.OAUTH2_ACCESS)
			.build();
		redisTokenRepository.saveToken(newAccessToken);

		if (tokenResponse.refreshToken() != null) {
			AuthToken newRefreshToken = AuthToken.builder()
				.userId(userId)
				.token(tokenResponse.refreshToken())
				.expiresIn(tokenResponse.refreshTokenExpiresIn())
				.type(AuthTokenType.OAUTH2_REFRESH)
				.build();
			redisTokenRepository.saveToken(newRefreshToken);
		}

		return tokenResponse.accessToken();
	}
}
