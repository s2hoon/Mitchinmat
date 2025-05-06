package com.mitchinmat.domain.place.api.dto.response;

import com.mitchinmat.domain.place.domain.Place;

public record PlaceResponse
	(
		long id,
		double x,
		double y,
		String placeName,
		String imageUrl
	) {

	public PlaceResponse(long id, double x, double y, String placeName, String imageUrl) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.placeName = placeName;
		this.imageUrl = imageUrl;
	}

	public static PlaceResponse of(Place place) {
		return new PlaceResponse(
			place.getId(),
			place.getX(),
			place.getY(),
			place.getPlaceName(),
			place.getPlaceUrl().getImageUrl()
		);
	}
}
