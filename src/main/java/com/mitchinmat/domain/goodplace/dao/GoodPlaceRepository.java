package com.mitchinmat.domain.goodplace.dao;

import static com.mitchinmat.global.error.ErrorCode.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mitchinmat.domain.goodplace.domain.GoodPlace;
import com.mitchinmat.global.error.ErrorCode;
import com.mitchinmat.global.error.exception.MitchinmatException;

@Repository
public interface GoodPlaceRepository extends JpaRepository<GoodPlace, Long>, GoodPlaceRepositoryCustom {

	boolean existsByUserIdAndPlaceId(Long userId, Long placeId);

	List<GoodPlace> findByUserId(Long userId);

	Optional<GoodPlace> findByUserIdAndPlaceId(Long userId, Long placeId);

	@Query("SELECT gp FROM GoodPlace gp JOIN FETCH gp.place p JOIN FETCH gp.user")
	List<GoodPlace> findAllGoodPlacesWithDetails();

	@Query("SELECT gp FROM GoodPlace gp " +
		"JOIN FETCH gp.place p " +
		"JOIN FETCH gp.user u " +
		"WHERE u.id = :userId " +
		"  AND gp.publicStatus = true")
	List<GoodPlace> findByUserIdAndPublicStatusTrue(@Param("userId") Long userId);

	default GoodPlace getByUserIdAndPlaceId(Long userId, Long placeId) {
		return findByUserIdAndPlaceId(userId, placeId)
			.orElseThrow(() -> new MitchinmatException(ErrorCode.GOOD_PLACE_NOT_EXIST));
	}

	default void checkDuplicateGoodPlace(Long userId, Long placeId) {
		if (existsByUserIdAndPlaceId(userId, placeId)) {
			throw new MitchinmatException(GOOD_PLACE_ALREADY_EXISTS);
		}
	}
}

