package com.mitchinmat.domain.goodplace.api;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mitchinmat.domain.goodplace.api.dto.request.GoodPlaceUpdateRequest;
import com.mitchinmat.domain.goodplace.api.dto.response.GoodPlaceResponse;
import com.mitchinmat.domain.goodplace.api.spec.GoodPlaceApiSpecification;
import com.mitchinmat.domain.goodplace.application.GoodPlaceService;
import com.mitchinmat.global.aop.LogExecution;
import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.common.response.ResponseUtils;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/good-place")
@LogExecution
public class GoodPlaceController implements GoodPlaceApiSpecification {

	private final GoodPlaceService goodPlaceService;

	@PostMapping("/place/{placeId}")
	public ApiResponse<Void> insertGoodPlace(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "placeId") Long placeId) {
		goodPlaceService.insertGoodPlace(customOAuth2User.getUserId(), placeId);
		return ResponseUtils.success();
	}

	@DeleteMapping("/{placeId}")
	public ApiResponse<Void> deleteGoodPlace(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "placeId") Long placeId) {
		goodPlaceService.deleteGoodPlace(customOAuth2User.getUserId(), placeId);
		return ResponseUtils.success();
	}

	@PatchMapping("/{placeId}/status")
	public ApiResponse<Map<String, Boolean>> updateGoodPlaceStatus(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "placeId") Long placeId) {
		return ResponseUtils.success("publicStatus",
			goodPlaceService.updateStatus(customOAuth2User.getUserId(), placeId));
	}

	@GetMapping("/user/me")
	public ApiResponse<List<GoodPlaceResponse>> getMyGoodPlaceList(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
		List<GoodPlaceResponse> goodPlaceResponseList = goodPlaceService.getGoodPlaceListByUserId(
			customOAuth2User.getUserId());
		return ResponseUtils.success(goodPlaceResponseList);
	}

	@PostMapping("/{goodPlaceId}/groups")
	public ApiResponse<Void> updateGoodPlaceGroups(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "goodPlaceId") Long placeId,
		@RequestBody GoodPlaceUpdateRequest goodPlaceUpdateRequest
	) {
		goodPlaceService.updateGoodPlaceGroups(
			customOAuth2User.getUserId(),
			placeId,
			goodPlaceUpdateRequest
		);
		return ResponseUtils.success();
	}

	@GetMapping("/user/{friendId}")
	public ApiResponse<List<GoodPlaceResponse>> getGoodPlacesByFriend(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "friendId") Long friendId) {
		List<GoodPlaceResponse> goodPlaceResponses = goodPlaceService.getGoodPlacesByFriend(
			customOAuth2User.getUserId(), friendId);
		return ResponseUtils.success(goodPlaceResponses);
	}

	@PatchMapping("/status")
	public ApiResponse<Map<String, Boolean>> updateGoodPlacesStatus(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
		return ResponseUtils.success("goodPlacesPublicStatus",
			goodPlaceService.updateGoodPlacesStatus(customOAuth2User.getUserId()));
	}
}

