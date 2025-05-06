package com.mitchinmat.domain.place.api.dto.response;

import com.mitchinmat.domain.place.domain.PlaceUrl;

public record PlaceUrlResponse(
	String placeUrl,
	String searchUrl,
	String roadFindUrl,
	String imageUrl
) {
	public static PlaceUrlResponse of(PlaceUrl placeUrl) {
		return new PlaceUrlResponse(
			placeUrl.getPlaceUrl(),
			placeUrl.getSearchUrl(),
			placeUrl.getRoadFindUrl(),
			placeUrl.getImageUrl()
		);
	}
}
