package com.mitchinmat.domain.place.api.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.mitchinmat.domain.place.domain.Place;

public record PlaceDetailResponse(
	long placeId,
	String placeName,
	String addressName,
	String roadAddressName,
	String phone,
	Double x,
	Double y,
	@JsonUnwrapped
	PlaceCategoryResponse placeCategoryResponse,
	@JsonUnwrapped
	PlaceUrlResponse placeUrlResponse,
	@JsonUnwrapped
	PlaceCrawledDataResponse placeCrawledDataResponse
) {

	public static PlaceDetailResponse of(Place place) {
		if (place == null) {
			return null;
		} else {
			return new PlaceDetailResponse(
				place.getId(),
				place.getPlaceName(),
				place.getAddressName(),
				place.getRoadAddressName(),
				place.getPhone(),
				place.getX(),
				place.getY(),
				PlaceCategoryResponse.of(place.getPlaceCategory()),
				PlaceUrlResponse.of(place.getPlaceUrl()),
				PlaceCrawledDataResponse.of(place.getPlaceCrawledData())
			);
		}
	}
}
