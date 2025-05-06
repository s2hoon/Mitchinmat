package com.mitchinmat.global.security.oauth2.handler;

import static com.mitchinmat.global.security.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository.*;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.mitchinmat.global.security.jwt.JwtTokenProvider;
import com.mitchinmat.global.security.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;
import com.mitchinmat.global.security.token.dao.RedisTokenRepository;
import com.mitchinmat.global.security.token.domain.AuthToken;
import com.mitchinmat.global.util.CookieUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtTokenProvider tokenProvider;
	private final RedisTokenRepository redisTokenRepository;
	private final HttpCookieOAuth2AuthorizationRequestRepository
		httpCookieOAuth2AuthorizationRequestRepository;

	@Value("${auth.token.refresh-cookie-key}")
	private String refreshCookieKey;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {

		String targetUrl = determineTargetUrl(request, response, authentication);
		setRefreshTokenCookie(authentication, response);
		if (response.isCommitted()) {
			return;
		}
		clearAuthenticationAttributes(request, response);
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) {
		Optional<String> redirectUri = CookieUtils.resolveCookie(request, REDIRECT_URL_PARAM_COOKIE_NAME)
			.map(Cookie::getValue);

		CustomOAuth2User customOAuth2User = (CustomOAuth2User)authentication.getPrincipal();
		String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

		return targetUrl;
	}

	private void setRefreshTokenCookie(Authentication authentication, HttpServletResponse response) {
		AuthToken refreshToken = tokenProvider.createRefreshToken(authentication);
		redisTokenRepository.saveToken(refreshToken);
		int maxAge = Math.toIntExact(refreshToken.getExpiresIn() / 1000);
		CookieUtils.setCookie(response, refreshCookieKey, refreshToken.getToken(), maxAge);
	}

	protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
		super.clearAuthenticationAttributes(request);
		httpCookieOAuth2AuthorizationRequestRepository.clearCookies(request, response);
	}
}
