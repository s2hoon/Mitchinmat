package com.mitchinmat.domain.group.api;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mitchinmat.domain.group.api.dto.request.GroupPlaceAddRequest;
import com.mitchinmat.domain.group.api.spec.GroupPlaceApiSpecification;
import com.mitchinmat.domain.group.application.GroupPlaceService;
import com.mitchinmat.global.aop.LogExecution;
import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.common.response.ResponseUtils;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v2/group-place")
@RequiredArgsConstructor
@LogExecution
public class GroupPlaceController implements GroupPlaceApiSpecification {

	private final GroupPlaceService groupPlaceService;

	@PostMapping("/{groupId}/place")
	public ApiResponse<Void> addPlacesToGroup(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable(name = "groupId") Long groupId,
		@RequestBody GroupPlaceAddRequest groupPlaceAddRequest
	) {
		groupPlaceService.addPlacesToGroup(customOAuth2User.getUserId(), groupId, groupPlaceAddRequest.placeIds());
		return ResponseUtils.success();
	}
    @DeleteMapping("/{groupId}/place/{placeId}")
	public ApiResponse<Void> removePlaceFromGroup(CustomOAuth2User customOAuth2User,
		@PathVariable(name = "groupId") Long groupId,
		@PathVariable(name = "placeId") Long placeId) {
		groupPlaceService.removePlaceFromGroup(customOAuth2User.getUserId(), groupId, placeId);
		return ResponseUtils.success();
	}

}
