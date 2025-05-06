package com.mitchinmat.domain.user.api;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mitchinmat.domain.user.api.dto.response.UserInfoResponse;
import com.mitchinmat.domain.user.api.spec.UserApiSpecification;
import com.mitchinmat.domain.user.application.UserService;
import com.mitchinmat.global.aop.LogExecution;
import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.common.response.ResponseUtils;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/user")
@LogExecution
public class UserController implements UserApiSpecification {

	private final UserService userService;

	@Override
	@GetMapping("/info")
	public ApiResponse<UserInfoResponse> getUserInfo(@AuthenticationPrincipal CustomOAuth2User user) {
		UserInfoResponse userInfoResponse = userService.findUserInfo(user.getUserId());
		return ResponseUtils.success(userInfoResponse);
	}

	@GetMapping("/kakao/friends-agreement")
	public ApiResponse<Map<String, Boolean>> getKakaoFriendsAgreement(@AuthenticationPrincipal CustomOAuth2User user) {
		return ResponseUtils.success("isAgreed", userService.checkKakaoFriendsAgreementScope(user.getUserId()));
	}

	@Override
	@PostMapping("/remove")
	public ApiResponse<Void> deleteUserAccount(HttpServletRequest request, HttpServletResponse response,
		@AuthenticationPrincipal CustomOAuth2User user) {
		userService.removeUser(request, response, user.getUserId());
		return ResponseUtils.success();
	}

}
