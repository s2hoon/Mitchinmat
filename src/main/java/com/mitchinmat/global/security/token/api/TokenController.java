package com.mitchinmat.global.security.token.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mitchinmat.global.aop.LogExecution;
import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.common.response.ResponseUtils;
import com.mitchinmat.global.security.token.application.TokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@LogExecution
@RequestMapping("/api/v2/token")
public class TokenController implements TokenApiSpecification {

	private final TokenService tokenService;

	@Override
	@PostMapping("/refresh")
	public ApiResponse<?> reissueAccessToken(HttpServletResponse response, HttpServletRequest request) {
		tokenService.reissueToken(request, response);
		return ResponseUtils.success();
	}

	@Override
	@PostMapping("/anonymous")
	public ApiResponse<?> issueAnonymousAccessToken() {
		return ResponseUtils.success("accessToken", tokenService.issueAnonymousAccessToken());
	}

}
