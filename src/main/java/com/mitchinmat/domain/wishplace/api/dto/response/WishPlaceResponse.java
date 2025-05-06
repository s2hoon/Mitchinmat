package com.mitchinmat.domain.wishplace.api.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.mitchinmat.domain.place.api.dto.response.PlaceCategoryResponse;
import com.mitchinmat.domain.place.domain.Place;
import com.mitchinmat.domain.wishplace.domain.WishPlace;

public record WishPlaceResponse(
	Long wishPlaceId,
	String placeName,
	String addressName,
	String roadAddressName,
	String phoneNumber,
	Double x,
	Double y,
	@JsonUnwrapped
	PlaceCategoryResponse categoryList,
	String imageUrl,
	LocalDateTime createdDate,
	LocalDateTime modifiedDate
) {
	public static WishPlaceResponse of(WishPlace wp) {
		Place place = wp.getPlace();
		return new WishPlaceResponse(
			place.getId(),
			place.getPlaceName(),
			place.getAddressName(),
			place.getRoadAddressName(),
			place.getPhone(),
			place.getX(),
			place.getY(),
			PlaceCategoryResponse.of(place.getPlaceCategory()),
			place.getPlaceUrl().getImageUrl(),
			wp.getCreatedDate(),
			wp.getModifiedDate());
	}
}
