package com.mitchinmat.domain.friend.api.spec;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Friend API", description = "친구 관련 API")
public interface FriendApiSpecifiaction {

	@Operation(summary = "친구 목록 동기화")
	ApiResponse<?> insertFriends(@AuthenticationPrincipal CustomOAuth2User user);

	@Operation(summary = "친구의 친구 목록 동기화")
	ApiResponse<?> insertFriendOfFriend(@AuthenticationPrincipal CustomOAuth2User user);

	@Operation(summary = "친구 목록 조회")
	ApiResponse<?> getFriends(@AuthenticationPrincipal CustomOAuth2User user);

	@Operation(summary = "친구의 맛집 지도 표시 여부 토글")
	ApiResponse<Map<String, Boolean>> updateFriendViewStatus(
		@AuthenticationPrincipal CustomOAuth2User user,
		@PathVariable("friendId") Long friendId);
}
