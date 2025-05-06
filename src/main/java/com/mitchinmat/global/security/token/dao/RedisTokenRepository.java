package com.mitchinmat.global.security.token.dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.mitchinmat.global.security.token.domain.AuthToken;
import com.mitchinmat.global.security.token.domain.AuthTokenType;
import com.mitchinmat.global.util.RedisUtil;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisTokenRepository {
	private final RedisUtil redisUtil;

	private String generateTokenKey(AuthTokenType type, Long userId) {
		return String.format("TOKEN:%s:%d", type.name(), userId);
	}

	public void saveToken(AuthToken authToken) {
		String key = generateTokenKey(authToken.getType(), authToken.getUserId());
		redisUtil.save(key, authToken.getToken(), authToken.getExpiresIn());
	}

	public Optional<String> findToken(AuthTokenType type, Long userId) {
		String key = generateTokenKey(type, userId);
		return redisUtil.find(key);
	}

	public void deleteToken(AuthTokenType type, Long userId) {
		String key = generateTokenKey(type, userId);
		redisUtil.delete(key);
	}

	public void deleteAllTokens(Long userId) {
		for (AuthTokenType type : AuthTokenType.values()) {
			String key = generateTokenKey(type, userId);
			redisUtil.delete(key);
		}
	}
}
