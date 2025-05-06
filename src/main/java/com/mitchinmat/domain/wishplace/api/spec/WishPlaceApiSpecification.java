package com.mitchinmat.domain.wishplace.api.spec;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.mitchinmat.domain.wishplace.api.dto.response.WishPlaceResponse;
import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "WishPlace API", description = "가고 싶은 맛집 관련 API")
public interface WishPlaceApiSpecification {

	@Operation(summary = "위시리스트 추가", description = "사용자가 특정 장소를 위시리스트에 추가합니다.")
	@PostMapping("/place/{placeId}")
	ApiResponse<Void> insertWishPlace(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "placeId") Long placeId
	);

	@Operation(summary = "위시리스트 삭제", description = "사용자가 위시리스트에서 특정 장소를 삭제합니다.")
	@DeleteMapping("/{wishPlaceId}")
	ApiResponse<Void> deleteWishPlace(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		 @PathVariable(name = "wishPlaceId") Long wishPlaceId
	);

	@Operation(summary = "내 위시리스트 조회", description = "현재 로그인한 사용자의 위시리스트 목록을 조회합니다.")
	@GetMapping("/user/me")
	ApiResponse<List<WishPlaceResponse>> getWishPlaceList(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User
	);
}