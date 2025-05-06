package com.mitchinmat.domain.groupmember.api;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mitchinmat.domain.groupmember.api.dto.res.GroupInviteResponse;
import com.mitchinmat.domain.groupmember.api.dto.res.GroupMemberResponse;
import com.mitchinmat.domain.groupmember.api.spec.GroupMemberApiSpecification;
import com.mitchinmat.domain.groupmember.application.GroupMemberService;
import com.mitchinmat.global.aop.LogExecution;
import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.common.response.ResponseUtils;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v2/group-member")
@RequiredArgsConstructor
@LogExecution
public class GroupMemberController implements GroupMemberApiSpecification {

	private final GroupMemberService groupMemberService;

	@Override
	@PostMapping("/{groupId}/invite-code")
	public ApiResponse<Map<String, String>> issueInviteCode(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "groupId") Long groupId
	) {
		return ResponseUtils.success("inviteCode",
			groupMemberService.issueInviteCode(customOAuth2User.getUserId(), groupId));
	}

	@Override
	@GetMapping("/code/{code}")
	public ApiResponse<GroupInviteResponse> getGroupInfoByInviteCode(
		@PathVariable(name = "code") String code
	) {
		GroupInviteResponse groupInfoByInviteCode = groupMemberService.getGroupInfoByInviteCode(code);
		return ResponseUtils.success(groupInfoByInviteCode);
	}

	@Override
	@PostMapping("/code/{code}/accept")
	public ApiResponse<Void> acceptInvite(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "code") String code
	) {
		groupMemberService.acceptInvite(customOAuth2User.getUserId(), code);
		return ResponseUtils.success();
	}

	@GetMapping("/{groupId}/member")
	public ApiResponse<List<GroupMemberResponse>> getGroupMembers(
		@PathVariable(name = "groupId") Long groupId) {
		List<GroupMemberResponse> members = groupMemberService.getGroupMembers(groupId);
		return ResponseUtils.success(members);
	}

	@DeleteMapping("/{groupId}/quit")
	public ApiResponse<Void> quitGroup(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "groupId") Long groupId) {
		groupMemberService.quitGroup(customOAuth2User.getUserId(), groupId);
		return ResponseUtils.success();
	}
}
