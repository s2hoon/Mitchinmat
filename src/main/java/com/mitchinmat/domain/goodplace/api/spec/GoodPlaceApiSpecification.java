package com.mitchinmat.domain.goodplace.api.spec;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.mitchinmat.domain.goodplace.api.dto.request.GoodPlaceUpdateRequest;
import com.mitchinmat.domain.goodplace.api.dto.response.GoodPlaceResponse;
import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "GoodPlace API", description = "맛집 관련 API")
public interface GoodPlaceApiSpecification {

	@Operation(summary = "나의 맛집 등록", description = "나의 맛집을 등록하는 API입니다.")
	ApiResponse<Void> insertGoodPlace(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "placeId") Long placeId);

	@Operation(summary = "나의 맛집 리스트 조회", description = "나의 맛집 리스트를 조회하는 API입니다.")
	ApiResponse<List<GoodPlaceResponse>> getMyGoodPlaceList(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User);

	@Operation(summary = "나의 맛집 공개/비공개 토글", description = "나의 맛집에 대한 공개와 비공개를 수정하는 API입니다.")
	ApiResponse<Map<String, Boolean>> updateGoodPlaceStatus(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "placeId") Long placeId);

	@Operation(summary = "나의 맛집 삭제", description = "나의 맛집을 삭제하는 API입니다.")
	ApiResponse<Void> deleteGoodPlace(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "placeId") Long placeId);

	@Operation(summary = "맛집 하나를 여러 그룹에 등록", description = "추가, 삭제할 그룹에 따라 맛집의 저장상태를 변경")
	ApiResponse<Void> updateGoodPlaceGroups(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "goodPlaceId") Long placeId,
		@RequestBody GoodPlaceUpdateRequest goodPlaceUpdateRequest
	);

	@Operation(summary = "친구의 맛집풀 조회", description = "한 친구의 맛집풀(공개된 맛집)을 조회한다.")
	ApiResponse<List<GoodPlaceResponse>> getGoodPlacesByFriend(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "friendId") Long friendId);

	@Operation(summary = "맛집풀 공개/비공개", description = "나의 맛집풀 공개/비공개 토글 여부를 변경한다.")
	ApiResponse<Map<String, Boolean>> updateGoodPlacesStatus(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User);
}
