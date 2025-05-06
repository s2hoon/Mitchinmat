package com.mitchinmat.domain.group.api.spec;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.mitchinmat.domain.group.api.dto.request.GroupPlaceAddRequest;
import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "GroupPlace API", description = "그룹 내 맛집 관리 API")
public interface GroupPlaceApiSpecification {

	@Operation(summary = "그룹에 맛집 다중 등록", description = "하나의 그룹에 여러 개의 맛집을 추가합니다.")
	ApiResponse<Void> addPlacesToGroup(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable Long groupId,
		@RequestBody GroupPlaceAddRequest groupPlaceAddRequest
	);

	@Operation(summary = "그룹에서 맛집 삭제", description = "그룹에서 특정 맛집을 제거합니다.")
	ApiResponse<Void> removePlaceFromGroup(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable Long groupId,
		@PathVariable Long placeId
	);
}