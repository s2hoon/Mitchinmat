package com.mitchinmat.domain.wishplace.dao;

import static com.mitchinmat.global.error.ErrorCode.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mitchinmat.domain.place.domain.Place;
import com.mitchinmat.domain.user.domain.User;
import com.mitchinmat.domain.wishplace.domain.WishPlace;
import com.mitchinmat.global.error.ErrorCode;
import com.mitchinmat.global.error.exception.MitchinmatException;

@Repository
public interface WishPlaceRepository extends JpaRepository<WishPlace, Long> {
	boolean existsByUserAndPlace(User user, Place place);
	List<WishPlace> findByUserId(Long userId);

	default WishPlace getById(Long id) {
		return findById(id).orElseThrow(() -> new MitchinmatException(WISH_PLACE_NOT_FOUND));
	}

	default void checkWishPlaceExists(User user, Place place) {
		if (existsByUserAndPlace(user, place)) {
			throw new MitchinmatException(ErrorCode.WISH_PLACE_ALREADY_EXISTS);
		}
	}
	
	default void validateWishPlaceOwner(WishPlace wishPlace, Long userId) {
		if (!wishPlace.getUser().getId().equals(userId)) {
			throw new MitchinmatException(ErrorCode.WISH_PLACE_FORBIDDEN);
		}
	}

	boolean existsByUserIdAndPlaceId(Long userId, Long placeId);
}
