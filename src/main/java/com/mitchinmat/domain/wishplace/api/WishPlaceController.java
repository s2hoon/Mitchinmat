package com.mitchinmat.domain.wishplace.api;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mitchinmat.domain.wishplace.api.dto.response.WishPlaceResponse;
import com.mitchinmat.domain.wishplace.api.spec.WishPlaceApiSpecification;
import com.mitchinmat.domain.wishplace.application.WishPlaceService;
import com.mitchinmat.global.aop.LogExecution;
import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.common.response.ResponseUtils;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/wish-place")
@LogExecution
public class WishPlaceController implements WishPlaceApiSpecification {

	private final WishPlaceService wishPlaceService;

	@PostMapping("/place/{placeId}")
	public ApiResponse<Void> insertWishPlace(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "placeId") Long placeId) {
		wishPlaceService.insertWishPlace(customOAuth2User.getUserId(), placeId);
		return ResponseUtils.success();
	}

	@DeleteMapping("/{wishPlaceId}")
	public ApiResponse<Void> deleteWishPlace(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "wishPlaceId") Long wishPlaceId) {
		wishPlaceService.deleteWishPlace(customOAuth2User.getUserId(), wishPlaceId);
		return ResponseUtils.success();
	}

	@GetMapping("/user/me")
	public ApiResponse<List<WishPlaceResponse>> getWishPlaceList(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User
	) {
		List<WishPlaceResponse> wishPlaces = wishPlaceService.getWishPlaceList(customOAuth2User.getUserId());
		return ResponseUtils.success(wishPlaces);
	}
}
