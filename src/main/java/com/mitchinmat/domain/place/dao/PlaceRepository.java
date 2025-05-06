package com.mitchinmat.domain.place.dao;

import static com.mitchinmat.global.error.ErrorCode.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mitchinmat.domain.place.domain.Place;
import com.mitchinmat.global.error.exception.MitchinmatException;

public interface PlaceRepository extends JpaRepository<Place, Long> {
	default Place getById(Long placeId) {
		return findById(placeId).orElseThrow(() -> new MitchinmatException(PLACE_NOT_EXIST));
	}
}
