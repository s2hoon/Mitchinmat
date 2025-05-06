package com.mitchinmat.domain.group.api;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mitchinmat.domain.group.api.dto.request.GroupInsertRequest;
import com.mitchinmat.domain.group.api.dto.request.GroupUpdateRequest;
import com.mitchinmat.domain.group.api.dto.response.FriendGroupListResponse;
import com.mitchinmat.domain.group.api.dto.response.GroupDetailResponse;
import com.mitchinmat.domain.group.api.dto.response.GroupListResponse;
import com.mitchinmat.domain.group.api.dto.response.GroupResponse;
import com.mitchinmat.domain.group.api.dto.response.ViewCodeResponse;
import com.mitchinmat.domain.group.api.spec.GroupApiSpecification;
import com.mitchinmat.domain.group.application.GroupService;
import com.mitchinmat.global.aop.LogExecution;
import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.common.response.ResponseUtils;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v2/group")
@RequiredArgsConstructor
@LogExecution
public class GroupController implements GroupApiSpecification {

	private final GroupService groupService;

	@PostMapping
	public ApiResponse<GroupResponse> insertGroup(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@Valid @RequestBody GroupInsertRequest groupInsertRequest) {
		return ResponseUtils.success(groupService.insertGroup(customOAuth2User.getUserId(), groupInsertRequest));
	}

	@PatchMapping("/{groupId}")
	public ApiResponse<GroupResponse> updateGroup(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "groupId") Long groupId,
		@Valid @RequestBody GroupUpdateRequest groupUpdateRequest) {
		return ResponseUtils.success(
			groupService.updateGroup(customOAuth2User.getUserId(), groupId, groupUpdateRequest));
	}

	@DeleteMapping("/{groupId}")
	public ApiResponse<Void> deleteGroup(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "groupId") Long groupId) {
		groupService.deleteGroup(customOAuth2User.getUserId(), groupId);
		return ResponseUtils.success();
	}

	@GetMapping("/user/me")
	public ApiResponse<GroupListResponse> getMyGroupList(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
		GroupListResponse groupListResponse = groupService.getMyGroupList(customOAuth2User.getUserId());
		return ResponseUtils.success(groupListResponse);
	}

	@PostMapping("/{groupId}")
	public ApiResponse<GroupResponse> copyGroup(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "groupId") Long groupId) {
		return ResponseUtils.success(groupService.copyGroup(customOAuth2User.getUserId(), groupId));
	}

	@GetMapping("/{groupId}/view-code")
	public ApiResponse<ViewCodeResponse> getGroupViewCode(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "groupId") Long groupId) {
		return ResponseUtils.success(groupService.getViewCode(customOAuth2User.getUserId(), groupId));
	}

	@GetMapping("/user/{friendId}")
	public ApiResponse<FriendGroupListResponse> getFriendGroupList(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "friendId") Long friendId) {
		FriendGroupListResponse friendGroupListResponse = groupService.getFriendGroupList(customOAuth2User.getUserId(), friendId);
		return ResponseUtils.success(friendGroupListResponse);
	}

	@GetMapping("/{groupId}/detail")
	public ApiResponse<GroupDetailResponse> getGroupDetail(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "groupId") Long groupId) {
		GroupDetailResponse groupDetailResponse = groupService.getGroupDetail(groupId);
		return ResponseUtils.success(groupDetailResponse);
	}

	@PostMapping("/{groupId}/status")
	public ApiResponse<Boolean> updateGroupStatus(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "groupId") Long groupId) {
		return ResponseUtils.success(groupService.updateGroupStatus(customOAuth2User.getUserId(), groupId));
	}
}
