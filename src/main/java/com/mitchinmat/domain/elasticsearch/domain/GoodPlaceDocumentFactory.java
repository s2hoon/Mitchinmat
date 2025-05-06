package com.mitchinmat.domain.elasticsearch.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mitchinmat.domain.goodplace.domain.GoodPlace;
import com.mitchinmat.domain.place.domain.PlaceCategory;

public class GoodPlaceDocumentFactory {
	public static GoodPlaceDocument createFromGoodPlace(GoodPlace goodPlace, Long userId) {
		Set<String> userIds = new HashSet<>();
		userIds.add(userId.toString());

		return GoodPlaceDocument.builder()
			.placeId(goodPlace.getPlace().getId())
			.userIds(userIds)
			.placeName(goodPlace.getPlace().getPlaceName())
			.addressName(goodPlace.getPlace().getAddressName())
			.roadAddressName(goodPlace.getPlace().getRoadAddressName())
			.placeCategory(goodPlace.getPlace().getPlaceCategory())
			.location(new GoodPlaceDocument.GeoPoint(goodPlace.getPlace().getY(), goodPlace.getPlace().getX()))
			.build();
	}

	public static GoodPlaceDocument updateWithUserId(GoodPlaceDocument existingDocument, Long userId) {
		Set<String> userIdSet = existingDocument.getUserIds();
		if (userIdSet == null) {
			userIdSet = new HashSet<>();
		}
		userIdSet.add(userId.toString());

		return GoodPlaceDocument.builder()
			.placeId(existingDocument.getPlaceId())
			.userIds(userIdSet)
			.placeName(existingDocument.getPlaceName())
			.addressName(existingDocument.getAddressName())
			.roadAddressName(existingDocument.getRoadAddressName())
			.placeCategory(existingDocument.getPlaceCategory())
			.location(existingDocument.getLocation())
			.build();
	}
}

