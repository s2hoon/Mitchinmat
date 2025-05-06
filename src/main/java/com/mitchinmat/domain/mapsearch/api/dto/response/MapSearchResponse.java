package com.mitchinmat.domain.mapsearch.api.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.mitchinmat.domain.place.domain.Place;
import com.mitchinmat.domain.wishplace.domain.WishPlace;

public record MapSearchResponse(
	List<PlaceSearchResponse> myPlaceList,
	List<PlaceSearchResponse> friendPlaceList,
	List<PlaceSearchResponse> friendOfFriendPlaceList,
	List<PlaceSearchResponse> wishPlaceList
) {
	public static MapSearchResponse of(
		List<Place> myPlaceList,
		List<Place> friendPlaceList,
		List<Place> friendOfFriendPlaceList,
		List<WishPlace> wishPlaceList) {
		return new MapSearchResponse(
			myPlaceList.stream().map(PlaceSearchResponse::fromPlace).collect(Collectors.toList()),
			friendPlaceList.stream().map(PlaceSearchResponse::fromPlace).collect(Collectors.toList()),
			friendOfFriendPlaceList.stream().map(PlaceSearchResponse::fromPlace).collect(Collectors.toList()),
			wishPlaceList.stream().map(PlaceSearchResponse::fromWishPlace).collect(Collectors.toList())
		);
	}
}
