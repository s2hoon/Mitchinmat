package com.mitchinmat.domain.groupmember.api.spec;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

import com.mitchinmat.domain.groupmember.api.dto.res.GroupInviteResponse;
import com.mitchinmat.domain.groupmember.api.dto.res.GroupMemberResponse;
import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "GroupMember API", description = "맛집 그룹 멤버 관련 API")
public interface GroupMemberApiSpecification {

	@Operation(summary = "그룹 참여 코드 발급", description = "한 그룹에 대한 참여 코드를 발급합니다.")
	ApiResponse<Map<String, String>> issueInviteCode(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "groupId") Long groupId
	);

	@Operation(summary = "초대 코드로 그룹 정보 조회", description = "초대 코드에 해당하는 그룹의 기본 정보를 조회합니다.")
	ApiResponse<GroupInviteResponse> getGroupInfoByInviteCode(
		@PathVariable(name = "code") String code
	);

	@Operation(summary = "초대 수락", description = "초대 코드를 사용하여 그룹 참여를 수락합니다.")
	ApiResponse<Void> acceptInvite(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "code") String code
	);

	@Operation(summary = "그룹 멤버 목록 조회", description = "한 그룹에 대한 멤버 목록을 조회합니다.")
	ApiResponse<List<GroupMemberResponse>> getGroupMembers(@PathVariable(name = "groupId") Long groupId);

	@Operation(summary = "그룹 나가기", description = "해당 그룹에서 탈퇴합니다.")
	ApiResponse<Void> quitGroup(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "groupId") Long groupId);
}
