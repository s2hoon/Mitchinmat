package com.mitchinmat.domain.mapsearch.api.dto.response;

import com.mitchinmat.domain.place.domain.Place;
import com.mitchinmat.domain.wishplace.domain.WishPlace;

public record PlaceSearchResponse(
	Long placeId,
	String placeName,
	Double x,
	Double y
){

	static PlaceSearchResponse fromPlace(Place place){
		return new PlaceSearchResponse(
			place.getId(),
			place.getPlaceName(),
			place.getX(),
			place.getY()
		);
	}

	static PlaceSearchResponse fromWishPlace(WishPlace wishPlace){
		Place place = wishPlace.getPlace();
		return fromPlace(place);
	}
}