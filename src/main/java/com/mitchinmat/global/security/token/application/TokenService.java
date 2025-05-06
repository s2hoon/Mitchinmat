package com.mitchinmat.global.security.token.application;

import static com.mitchinmat.global.error.ErrorCode.*;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mitchinmat.global.error.exception.NonLoggableException;
import com.mitchinmat.global.security.jwt.JwtTokenProvider;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;
import com.mitchinmat.global.security.token.dao.RedisTokenRepository;
import com.mitchinmat.global.security.token.domain.AuthToken;
import com.mitchinmat.global.security.token.domain.AuthTokenType;
import com.mitchinmat.global.util.CookieUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {

	private final JwtTokenProvider tokenProvider;
	private final RedisTokenRepository redisTokenRepository;

	@Value("${auth.token.refresh-cookie-key}")
	private String cookieKey;

	@Transactional
	public void reissueToken(HttpServletRequest request, HttpServletResponse response) {
		String cookieRefreshToken = CookieUtils.resolveCookie(request, cookieKey)
			.map(Cookie::getValue).orElseThrow(() -> new NonLoggableException(REFRESH_TOKEN_NOT_EXIST_IN_COOKIE));

		Authentication authentication = tokenProvider.getAuthentication(cookieRefreshToken);

		validateToken(cookieRefreshToken, authentication);

		AuthToken accessToken = tokenProvider.createAccessToken(authentication);
		response.setHeader("Authorization", "Bearer " + accessToken.getToken());
	}

	public String issueAnonymousAccessToken() {
		CustomOAuth2User anonymousUser = CustomOAuth2User.of(
			999L,
			"anonymous-oauth2-id",
			Collections.singletonList(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))
		);

		Authentication authentication = new UsernamePasswordAuthenticationToken(
			anonymousUser,
			null,
			anonymousUser.getAuthorities()
		);
		return tokenProvider.createAccessToken(authentication).getToken();
	}

	private void validateToken(String oldToken, Authentication authentication) {
		if (!tokenProvider.validateToken(oldToken)) {
			throw new NonLoggableException(REFRESH_TOKEN_EXPIRED);
		}

		String savedRefreshToken = getSavedRefreshToken(authentication);
		if (!oldToken.equals(savedRefreshToken)) {
			throw new NonLoggableException(TOKEN_NOT_MATCHED);
		}
	}

	private String getSavedRefreshToken(Authentication authentication) {
		CustomOAuth2User user = (CustomOAuth2User)authentication.getPrincipal();
		return redisTokenRepository.findToken(AuthTokenType.REFRESH, user.getUserId())
			.orElseThrow(() -> new NonLoggableException(REFRESH_TOKEN_EXPIRED));
	}

}
