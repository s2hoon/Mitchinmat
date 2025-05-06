package com.mitchinmat.domain.comment.api.spec;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.mitchinmat.domain.comment.api.dto.request.CommentRequest;
import com.mitchinmat.domain.comment.api.dto.response.CommentResponse;
import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Comment API", description = "댓글 관련 기능을 제공하는 API입니다.")
public interface CommentApiSpecification {

	@Operation(
		summary = "댓글 작성",
		description = "특정 장소에 대해 사용자가 댓글을 작성합니다. 댓글 작성자는 인증된 사용자여야 합니다."
	)
	ApiResponse<CommentResponse> insertComment(
		@AuthenticationPrincipal CustomOAuth2User user,
		@PathVariable Long placeId,
		@RequestBody CommentRequest commentRequest
	);

	@Operation(
		summary = "댓글 수정",
		description = "기존 댓글을 수정합니다. 댓글 수정자는 해당 댓글을 작성한 사용자여야 하며, 인증이 필요합니다."
	)
	ApiResponse<Void> updateComment(
		@AuthenticationPrincipal CustomOAuth2User user,
		@PathVariable Long commentId,
		@RequestBody CommentRequest commentRequest
	);

	@Operation(
		summary = "댓글 삭제",
		description = "특정 댓글을 삭제합니다. 댓글 삭제자는 해당 댓글을 작성한 사용자여야 하며, 인증이 필요합니다."
	)
	ApiResponse<Void> deleteComment(
		@AuthenticationPrincipal CustomOAuth2User user,
		@PathVariable Long commentId
	);

	@Operation(
		summary = "특정 음식점에 달린 친구 댓글 가져오기",
		description = "특정 음식점에서 내 친구들이 단 댓글을 가져옵니다."
	)
	ApiResponse<List<CommentResponse>> getCommentsByPlace(
		@AuthenticationPrincipal CustomOAuth2User user,
		@PathVariable Long placeId
	);
}
