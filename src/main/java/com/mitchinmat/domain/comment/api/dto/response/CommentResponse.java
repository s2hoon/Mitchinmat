package com.mitchinmat.domain.comment.api.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mitchinmat.domain.comment.domain.Comment;
import com.mitchinmat.domain.user.domain.User;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CommentResponse(
	Long commentId,
	String userName,
	String profileImage,
	String content,
	LocalDateTime modifiedDate,
	Long userId
) {
	public static CommentResponse of(Comment comment, User user) {
		return new CommentResponse(comment.getId(), user.getUsername(), user.getProfileImage(), comment.getContent(),
			comment.getModifiedDate(), user.getId());
	}

	public static CommentResponse of(Comment comment, String userName, String profileImage, Long userId) {
		return new CommentResponse(comment.getId(), userName, profileImage, comment.getContent(),
			comment.getModifiedDate(), userId);
	}

	public static CommentResponse of(Comment comment){
		return CommentResponse.of(comment, comment.getUser());
	}
}
