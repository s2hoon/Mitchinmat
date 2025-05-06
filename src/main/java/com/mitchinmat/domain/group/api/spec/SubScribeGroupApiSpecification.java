package com.mitchinmat.domain.group.api.spec;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "SubscribeGroup API", description = "그룹 구독 관련 API")
public interface SubScribeGroupApiSpecification {

	@Operation(summary = "그룹 구독", description = "특정 그룹을 구독합니다.")
	ApiResponse<Void> subscribeGroup(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable Long groupId
	);

	@Operation(summary = "그룹 구독 해제", description = "구독 중인 그룹을 구독 해제합니다.")
	ApiResponse<Void> unsubscribeGroup(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable Long groupId
	);
}
