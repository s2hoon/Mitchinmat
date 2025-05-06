package com.mitchinmat.domain.comment.api;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mitchinmat.domain.comment.api.dto.request.CommentRequest;
import com.mitchinmat.domain.comment.api.dto.response.CommentResponse;
import com.mitchinmat.domain.comment.api.spec.CommentApiSpecification;
import com.mitchinmat.domain.comment.application.CommentService;
import com.mitchinmat.domain.comment.domain.Comment;
import com.mitchinmat.domain.user.domain.User;
import com.mitchinmat.global.aop.LogExecution;
import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.common.response.ResponseUtils;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/comment")
@Slf4j
@LogExecution
public class CommentController implements CommentApiSpecification {

	private final CommentService commentService;

	@Override
	@PostMapping("/place/{placeId}")
	public ApiResponse<CommentResponse> insertComment(@AuthenticationPrincipal CustomOAuth2User user,
		@PathVariable("placeId") Long placeId,
		@RequestBody CommentRequest commentRequest) {
		return ResponseUtils.success(commentService.insertComment(commentRequest, user.getUserId(), placeId));
	}

	@Override
	@PutMapping("/{commentId}")
	public ApiResponse<Void> updateComment(@AuthenticationPrincipal CustomOAuth2User user,
		@PathVariable("commentId") Long commentId, @RequestBody CommentRequest commentRequest) {
		commentService.updateComment(user.getUserId(), commentId, commentRequest.content());
		return ResponseUtils.success();
	}

	@Override
	@DeleteMapping("/{commentId}")
	public ApiResponse<Void> deleteComment(@AuthenticationPrincipal CustomOAuth2User user,
		@PathVariable("commentId") Long commentId) {
		commentService.deleteComment(user.getUserId(), commentId);
		return ResponseUtils.success();
	}

	@Override
	@GetMapping("/place/{placeId}")
	public ApiResponse<List<CommentResponse>> getCommentsByPlace(@AuthenticationPrincipal CustomOAuth2User user,
		@PathVariable("placeId") Long placeId) {
		return ResponseUtils.success(commentService.getCommentsByPlace(user.getUserId(), placeId));
	}

}
