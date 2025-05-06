package com.mitchinmat.domain.user.dao;

import static com.mitchinmat.global.error.ErrorCode.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mitchinmat.domain.friend.domain.Friend;
import com.mitchinmat.domain.user.domain.User;
import com.mitchinmat.global.error.exception.MitchinmatException;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByOauth2Id(String oauth2Id);

	@Query("SELECT gp.user FROM GoodPlace gp WHERE gp.publicStatus = true and gp.place.id = :placeId AND gp.user.id IN (SELECT f.friendId FROM Friend f WHERE f IN :friends)")
	List<User> findUsersByPlaceIdAndFriends(@Param("placeId") Long placeId, @Param("friends") List<Friend> friends);

	default User getById(Long userId) {
		return findById(userId).orElseThrow(() -> new MitchinmatException(USER_NOT_EXIST));
	}
}

