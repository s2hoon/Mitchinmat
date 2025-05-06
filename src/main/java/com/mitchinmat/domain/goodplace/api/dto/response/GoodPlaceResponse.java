package com.mitchinmat.domain.goodplace.api.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.mitchinmat.domain.goodplace.domain.GoodPlace;
import com.mitchinmat.domain.place.api.dto.response.PlaceCategoryResponse;
import com.mitchinmat.domain.place.domain.Place;

public record GoodPlaceResponse(
	Long placeId,
	String placeName,
	String addressName,
	String roadAddressName,
	String phoneNumber,
	Double x,
	Double y,
	@JsonUnwrapped
	PlaceCategoryResponse categoryList,
	String imageUrl,
	Boolean publicStatus,
	LocalDateTime createdDate,
	LocalDateTime modifiedDate
) {
	public static GoodPlaceResponse of(GoodPlace gp) {
		Place place = gp.getPlace();
		return new GoodPlaceResponse(
			place.getId(),
			place.getPlaceName(),
			place.getAddressName(),
			place.getRoadAddressName(),
			place.getPhone(),
			place.getX(),
			place.getY(),
			PlaceCategoryResponse.of(place.getPlaceCategory()),
			place.getPlaceUrl().getImageUrl(),
			gp.isPublicStatus(),
			gp.getCreatedDate(),
			gp.getModifiedDate());
	}
}
