package com.mitchinmat.domain.mapsearch.api;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mitchinmat.domain.mapsearch.api.dto.request.PlaceSearchRequest;
import com.mitchinmat.domain.mapsearch.api.dto.response.MapSearchResponse;
import com.mitchinmat.domain.mapsearch.api.dto.response.PlaceInfoDetailResponse;
import com.mitchinmat.domain.mapsearch.api.dto.response.PlaceInfoResponse;
import com.mitchinmat.domain.mapsearch.api.spec.MapSearchApiSpecification;
import com.mitchinmat.domain.mapsearch.application.MapSearchService;
import com.mitchinmat.global.aop.LogExecution;
import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.common.response.ResponseUtils;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/search")
@LogExecution
public class MapSearchController implements MapSearchApiSpecification {

	private final MapSearchService mapSearchService;

	@GetMapping("/map")
	public ApiResponse<MapSearchResponse> searchPlaceOnMap(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@RequestParam(required = false) Double top,
		@RequestParam(required = false) Double bottom,
		@RequestParam(required = false) Double left,
		@RequestParam(required = false) Double right,
		@RequestParam(required = false) String query
		) {
		Long userId = customOAuth2User.getUserId();
		return ResponseUtils.success(
			mapSearchService.searchOnMap(userId, PlaceSearchRequest.of(top, bottom, left, right, query)));
	}

	@GetMapping("/filter")
	public ApiResponse<Void> searchPlaceByGroup(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@RequestParam(name = "group") String groupId) {
		return ResponseUtils.success();
	}

	@GetMapping("/place/{placeId}")
	public ApiResponse<PlaceInfoResponse> getPlaceInfo(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable Long placeId) {
		Long userId = customOAuth2User.getUserId();
		return ResponseUtils.success(
			mapSearchService.getPlaceInfo(userId, placeId)
		);
	}

	@GetMapping("/place/{placeId}/detail")
	public ApiResponse<PlaceInfoDetailResponse> getPlaceDetail(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable Long placeId){
		Long userId = customOAuth2User.getUserId();
		return ResponseUtils.success(
			mapSearchService.getPlaceInfoDetail(userId, placeId)
		);
	}
}
