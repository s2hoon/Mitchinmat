package com.mitchinmat.domain.comment.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mitchinmat.domain.comment.api.dto.request.CommentRequest;
import com.mitchinmat.domain.comment.api.dto.response.CommentResponse;
import com.mitchinmat.domain.comment.dao.CommentRepository;
import com.mitchinmat.domain.comment.domain.Comment;
import com.mitchinmat.domain.friend.dao.FriendRepository;
import com.mitchinmat.domain.friend.domain.Friend;
import com.mitchinmat.domain.place.dao.PlaceRepository;
import com.mitchinmat.domain.place.domain.Place;
import com.mitchinmat.domain.user.dao.UserRepository;
import com.mitchinmat.domain.user.domain.User;
import com.mitchinmat.global.error.ErrorCode;
import com.mitchinmat.global.error.exception.MitchinmatException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

	private final CommentRepository commentRepository;
	private final PlaceRepository placeRepository;
	private final UserRepository userRepository;
	private final FriendRepository friendRepository;

	@Transactional
	public CommentResponse insertComment(CommentRequest content, Long userId, Long placeId) {
		Place place = placeRepository.findById(placeId)
			.orElseThrow(() -> new MitchinmatException(ErrorCode.PLACE_NOT_EXIST));
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new MitchinmatException(ErrorCode.USER_NOT_EXIST));
		try {
			Comment comment = Comment.builder()
				.content(content.content())
				.place(place)
				.user(user)
				.build();
			place.addComment(comment);
			user.addComment(comment);
			return CommentResponse.of(commentRepository.save(comment), user);
		} catch (Exception e) {
			throw new MitchinmatException(ErrorCode.COMMENT_CREATE_FAILED);
		}
	}

	@Transactional
	public void updateComment(Long userId, Long commentId, String newContent) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new MitchinmatException(ErrorCode.COMMENT_NOT_EXIST));
		if (!(comment.getUser().getId() == userId)) {
			throw new MitchinmatException(ErrorCode.AUTH_FAILED);
		}
		try {
			comment.updateContent(newContent);
			commentRepository.save(comment);
		} catch (Exception e) {
			throw new MitchinmatException(ErrorCode.COMMENT_UPDATE_FAILED);
		}
	}

	@Transactional
	public void deleteComment(Long userId, Long commentId) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new MitchinmatException(ErrorCode.COMMENT_NOT_EXIST));
		if (!(comment.getUser().getId() == userId)) {
			throw new MitchinmatException(ErrorCode.AUTH_FAILED);
		}
		try {
			Place place = comment.getPlace();
			if (place != null) {
				place.removeComment(comment);
			}
			User user = comment.getUser();
			if (user != null) {
				user.removeComment(comment);
			}
			commentRepository.delete(comment);
		} catch (Exception e) {
			throw new MitchinmatException(ErrorCode.COMMENT_DELETE_FAILED);
		}
	}

	public List<CommentResponse> getCommentsByPlace(Long userId, Long placeId) {
		List<Long> friendList = friendRepository.findFriendIdsByUserId(userId);

		return commentRepository.findByUserIdInAndPlaceId(friendList, placeId).stream().map(CommentResponse::of).collect(
			Collectors.toList());
	}
}
