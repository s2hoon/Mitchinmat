package com.mitchinmat.domain.comment.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mitchinmat.domain.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByUserIdInAndPlaceId(List<Long> userIds, Long placeId);

}
