package com.mitchinmat.domain.friend.api;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mitchinmat.domain.friend.api.dto.response.FriendDetailListResponse;
import com.mitchinmat.domain.friend.api.dto.response.FriendOfFriendResponse;
import com.mitchinmat.domain.friend.api.spec.FriendApiSpecifiaction;
import com.mitchinmat.domain.friend.application.FriendKakaoSyncService;
import com.mitchinmat.domain.friend.application.FriendService;
import com.mitchinmat.global.aop.LogExecution;
import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.common.response.ResponseUtils;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@LogExecution
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/friend")
public class FriendController implements FriendApiSpecifiaction {

	private final FriendService friendService;
	private final FriendKakaoSyncService friendKakaoSyncService;

	@Override
	@LogExecution
	@GetMapping
	public ApiResponse<FriendDetailListResponse> getFriends(@AuthenticationPrincipal CustomOAuth2User user) {
		return ResponseUtils.success(friendService.getFriends(user.getUserId()));
	}

	@PatchMapping("/{friendId}/view-status")
	public ApiResponse<Map<String, Boolean>> updateFriendViewStatus(
		@AuthenticationPrincipal CustomOAuth2User user,
		@PathVariable(name = "friendId") Long friendId) {
		return ResponseUtils.success("viewStatus", friendService.updateFriendViewStatus(user.getUserId(), friendId));
	}

	@Override
	@PostMapping("/sync")
	public ApiResponse<Void> insertFriends(@AuthenticationPrincipal CustomOAuth2User user) {
		friendKakaoSyncService.syncFriends(user.getUserId());
		return ResponseUtils.success();
	}

	@Override
	@PostMapping("/sync/friend-of-friend")
	public ApiResponse<FriendOfFriendResponse> insertFriendOfFriend(@AuthenticationPrincipal CustomOAuth2User user) {
		return ResponseUtils.success(friendService.syncFriendOfFriend(user.getUserId()));
	}

}
