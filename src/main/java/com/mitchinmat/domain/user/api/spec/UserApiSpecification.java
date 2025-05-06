package com.mitchinmat.domain.user.api.spec;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.mitchinmat.domain.user.api.dto.response.UserInfoResponse;
import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "User API", description = "사용자 관련 API")
public interface UserApiSpecification {

	@Operation(summary = "유저 정보 조회", description = "사용자의 세부 정보를 조회합니다.")
	ApiResponse<UserInfoResponse> getUserInfo(@AuthenticationPrincipal CustomOAuth2User user);

	@Operation(summary = "카카오 친구 목록 제공 동의 여부 확인", description = "사용자의 카카오 친구 목록 제공 동의 여부를 확인합니다.")
	ApiResponse<Map<String, Boolean>> getKakaoFriendsAgreement(@AuthenticationPrincipal CustomOAuth2User user);

	@Operation(summary = "유저 탈퇴", description = "사용자의 정보를 삭제합니다.")
	ApiResponse<Void> deleteUserAccount(HttpServletRequest request, HttpServletResponse response,
		@AuthenticationPrincipal CustomOAuth2User user);
}
