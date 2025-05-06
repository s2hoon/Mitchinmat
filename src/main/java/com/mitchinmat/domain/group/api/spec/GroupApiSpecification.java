package com.mitchinmat.domain.group.api.spec;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.mitchinmat.domain.group.api.dto.request.GroupInsertRequest;
import com.mitchinmat.domain.group.api.dto.request.GroupUpdateRequest;
import com.mitchinmat.domain.group.api.dto.response.FriendGroupListResponse;
import com.mitchinmat.domain.group.api.dto.response.GroupDetailResponse;
import com.mitchinmat.domain.group.api.dto.response.GroupListResponse;
import com.mitchinmat.domain.group.api.dto.response.GroupResponse;
import com.mitchinmat.domain.group.api.dto.response.ViewCodeResponse;
import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Group API", description = "맛집 그룹 관련 API")
public interface GroupApiSpecification {

	@Operation(summary = "그룹 생성", description = "그룹을 생성합니다")
	ApiResponse<GroupResponse> insertGroup(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@RequestBody GroupInsertRequest groupInsertRequest);

	@Operation(summary = "그룹 복제", description = "기존 그룹을 복제하여 내 그룹 리스트로 이동합니다")
	ApiResponse<GroupResponse> copyGroup(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@RequestBody Long groupId);

	@Operation(summary = "보기 전용 링크 코드 발급", description = "보기 전용 링크 코드를 발급합니다.")
	ApiResponse<ViewCodeResponse> getGroupViewCode(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable Long groupId);

	@Operation(summary = "그룹 수정", description = "groupId의 그룹을 수정합니다.")
	ApiResponse<GroupResponse> updateGroup(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable Long groupId,
		@RequestBody GroupUpdateRequest groupUpdateRequest);

	@Operation(summary = "그룹 삭제", description = "groupId의 그룹을 삭제합니다.")
	ApiResponse<Void> deleteGroup(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable Long groupId);

	@Operation(summary = "나의 모든 그룹 목록 조회", description = "내가 보유한 모든 그룹의 목록을 조회합니다.")
	ApiResponse<GroupListResponse> getMyGroupList(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User);

	@Operation(summary = "친구의 그룹 목록 조회", description = "친구의 모든 그룹 목록을 조회합니다.")
	ApiResponse<FriendGroupListResponse> getFriendGroupList(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable Long friendId);

	@Operation(summary = "그룹 상세 조회", description = "그룹 내 모든 맛집 리스트를 조회합니다.")
	ApiResponse<GroupDetailResponse> getGroupDetail(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable Long groupId);

	@Operation(summary = "그룹 공개/비공개 변경", description = "토글 형태로 그룹의 공개 비공개 상태 변경")
	ApiResponse<Boolean> updateGroupStatus(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable Long groupId);
	
}
