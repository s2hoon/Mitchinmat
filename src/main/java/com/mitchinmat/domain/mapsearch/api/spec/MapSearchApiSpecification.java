package com.mitchinmat.domain.mapsearch.api.spec;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.mitchinmat.domain.mapsearch.api.dto.request.PlaceSearchRequest;
import com.mitchinmat.domain.mapsearch.api.dto.response.MapSearchResponse;
import com.mitchinmat.domain.mapsearch.api.dto.response.PlaceInfoDetailResponse;
import com.mitchinmat.domain.mapsearch.api.dto.response.PlaceInfoResponse;
import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "MapSearch API", description = "지도 맛집 검색 API")
public interface MapSearchApiSpecification {

	@Operation(summary = "지도에서 맛집 검색", description = "사용자 범위(내, 친구, 친구의 친구)에 따른 맛집 검색을 수행합니다.")
	ApiResponse<MapSearchResponse> searchPlaceOnMap(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@RequestParam(required = false) Double top,
		@RequestParam(required = false) Double bottom,
		@RequestParam(required = false) Double left,
		@RequestParam(required = false) Double right,
		@RequestParam(required = false) String query
	);

	@Operation(summary = "그룹 필터 검색", description = "그룹을 기반으로 맛집 검색을 수행합니다.")
	ApiResponse<Void> searchPlaceByGroup(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@RequestParam("group") String groupId);

	@Operation(summary = "맛집 기본 조회", description = "맛집의 기본 정보를 조회합니다")
	ApiResponse<PlaceInfoResponse> getPlaceInfo(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "placeId") Long placeId);

	@Operation(summary = "맛집 상세 조회", description = "맛집의 상세 정보를 조회합니다.")
	ApiResponse<PlaceInfoDetailResponse> getPlaceDetail(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "placeId") Long placeId);

}

