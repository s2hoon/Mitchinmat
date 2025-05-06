package com.mitchinmat.domain.goodplace.dao;

import java.util.List;

import com.mitchinmat.domain.goodplace.domain.GoodPlace;
import com.mitchinmat.domain.place.domain.Place;
import com.mitchinmat.domain.mapsearch.api.dto.request.PlaceSearchRequest;

public interface GoodPlaceRepositoryCustom {

	List<Place> findGoodPlacesInBounds(
		Long userId, PlaceSearchRequest placeSearchRequest);

	List<Place> findFriendGoodPlacesInBouds(
		List<Long> userIds, PlaceSearchRequest placeSearchRequest);

	List<Place> findFriendOfFriendPlacesInBounds(
		List<Long> userIds, PlaceSearchRequest placeSearchRequest);
}
