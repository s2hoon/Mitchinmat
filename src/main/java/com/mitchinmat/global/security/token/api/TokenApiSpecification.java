package com.mitchinmat.global.security.token.api;

import com.mitchinmat.global.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "Token API", description = "토큰 관련 API")
public interface TokenApiSpecification {

	@Operation(summary = "Access Token 재발급", description = "Access Token 만료 시, Refresh Token으로 재발급합니다.")
	ApiResponse<?> reissueAccessToken(HttpServletResponse response, HttpServletRequest request);

	@Operation(summary = "익명 Access Token 발급", description = "로그인을 안한 사용자를 위한 익명 토큰을 발급합니다.")
	ApiResponse<?> issueAnonymousAccessToken();
}
