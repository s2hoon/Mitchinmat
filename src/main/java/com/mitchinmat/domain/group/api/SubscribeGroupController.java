package com.mitchinmat.domain.group.api;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mitchinmat.domain.group.api.spec.SubScribeGroupApiSpecification;
import com.mitchinmat.domain.group.application.SubscribeGroupService;
import com.mitchinmat.global.aop.LogExecution;
import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.common.response.ResponseUtils;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v2/subscribe")
@RequiredArgsConstructor
@LogExecution
public class SubscribeGroupController implements SubScribeGroupApiSpecification {

	private final SubscribeGroupService subscribeGroupService;

	@PostMapping("/{groupId}")
	public ApiResponse<Void> subscribeGroup(CustomOAuth2User customOAuth2User,
		@PathVariable(name = "groupId") Long groupId) {
		subscribeGroupService.subscribe(customOAuth2User.getUserId(), groupId);
		return ResponseUtils.success();
	}

	@DeleteMapping("/{groupId}")
	public ApiResponse<Void> unsubscribeGroup(CustomOAuth2User customOAuth2User,
		@PathVariable(name = "groupId") Long groupId) {
		subscribeGroupService.unsubscribe(customOAuth2User.getUserId(), groupId);
		return ResponseUtils.success();
	}
}
