package com.mitchinmat.domain.friend.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mitchinmat.domain.friend.domain.Friend;
import com.mitchinmat.global.error.ErrorCode;
import com.mitchinmat.global.error.exception.MitchinmatException;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long>, FriendRepositoryCustom {

	List<Friend> findAllByUserId(Long userId);

	long countByUserIdAndFriendId(Long userId, Long friendId);

	Optional<Friend> findByUserIdAndFriendId(Long userId, Long friendId);

	@Query("SELECT f.friendId FROM Friend f WHERE f.userId = :userId")
	List<Long> findFriendIds(@Param("userId") Long userId);

	@Query("SELECT f.friendId FROM Friend f WHERE f.userId = :userId AND f.viewStatus = true")
	List<Long> findActiveFriendIds(@Param("userId") Long userId);

	default Friend getByUserIdAndFriendId(Long userId, Long friendId) {
		return findByUserIdAndFriendId(userId, friendId)
			.orElseThrow(() -> new MitchinmatException(ErrorCode.FRIEND_NOT_EXIST));
	}
}

