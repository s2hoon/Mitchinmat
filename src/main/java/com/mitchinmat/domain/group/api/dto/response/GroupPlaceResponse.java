package com.mitchinmat.domain.group.api.dto.response;

import com.mitchinmat.domain.place.api.dto.response.PlaceDetailResponse;

public record GroupPlaceResponse(
	long groupId,
	long userId,
	PlaceDetailResponse placeDetail
) {
}
