package com.mitchinmat.domain.goodplace.dao;

import static com.mitchinmat.domain.goodplace.domain.QGoodPlace.*;
import static com.mitchinmat.domain.place.domain.QPlace.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mitchinmat.domain.goodplace.domain.GoodPlace;
import com.mitchinmat.domain.mapsearch.api.dto.request.PlaceSearchRequest;
import com.mitchinmat.domain.place.domain.Place;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GoodPlaceRepositoryCustomImpl implements GoodPlaceRepositoryCustom{

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Place> findGoodPlacesInBounds(
		Long userId, PlaceSearchRequest placeSearchRequest) {
		return queryFactory
			.select(goodPlace.place)
			.from(goodPlace)
			.join(goodPlace.place, place)
			.where(
				goodPlace.user.id.eq(userId)
					.and(place.x.between(placeSearchRequest.left(), placeSearchRequest.right()))
					.and(place.y.between(placeSearchRequest.bottom(), placeSearchRequest.top()))
					.and(place.placeName.contains(placeSearchRequest.query()))
			)
			.fetch();
	}

	@Override
	public List<Place> findFriendGoodPlacesInBouds(
		List<Long> userIds, PlaceSearchRequest placeSearchRequest) {
		return queryFactory
			.select(goodPlace.place)
			.from(goodPlace)
			.join(goodPlace).on(goodPlace.place.id.eq(place.id))
			.where(
				goodPlace.user.id.in(userIds)
					.and(goodPlace.publicStatus.isTrue())
					.and(place.x.between(placeSearchRequest.left(), placeSearchRequest.right()))
					.and(place.y.between(placeSearchRequest.bottom(), placeSearchRequest.top()))
					.and(place.placeName.contains(placeSearchRequest.query()))
			)
			.fetch();
	}

	@Override
	public List<Place> findFriendOfFriendPlacesInBounds(
		List<Long> userIds, PlaceSearchRequest placeSearchRequest) {
		return queryFactory
			.select(goodPlace.place)
			.from(goodPlace)
			.join(goodPlace).on(goodPlace.place.id.eq(place.id))
			.where(
				goodPlace.user.id.in(userIds)
					.and(goodPlace.publicStatus.isTrue())
					.and(place.x.between(placeSearchRequest.left(), placeSearchRequest.right()))
					.and(place.y.between(placeSearchRequest.bottom(), placeSearchRequest.top()))
					.and(place.placeName.contains(placeSearchRequest.query()))
			)
			.fetch();
	}
}
