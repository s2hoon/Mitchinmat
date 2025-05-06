package com.mitchinmat.global.util;

import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.util.SerializationUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CookieUtils {

	static Optional<Cookie> resolveCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();

		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName)) {
					return Optional.of(cookie);
				}
			}
		}

		return Optional.empty();
	}

	static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
		Optional<Cookie> optionalCookie = resolveCookie(request, cookieName);
		if (optionalCookie.isPresent()) {
			Cookie cookie = optionalCookie.get();
			cookie.setValue("");
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
	}

	static void setCookie(HttpServletResponse response,
		String cookieName, String cookieContents, int maxAge) {
		ResponseCookie cookie = ResponseCookie.from(cookieName, cookieContents)
			.path("/")
			.sameSite("None")
			.httpOnly(false)
			.secure(true)
			.maxAge(maxAge)
			.build();
		response.addHeader("Set-Cookie", cookie.toString());
	}

	static String serialize(OAuth2AuthorizationRequest request) {
		return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(request));
	}

	static <T> T deserialize(Cookie cookie, Class<T> clz) {
		if (isDeleted(cookie))
			return null;
		return clz.cast(SerializationUtils.deserialize(
			Base64.getUrlDecoder().decode(cookie.getValue()))
		);
	}

	private static boolean isDeleted(Cookie cookie) {
		return StringUtils.isBlank(cookie.getValue()) || Objects.isNull(cookie.getValue());
	}
}
