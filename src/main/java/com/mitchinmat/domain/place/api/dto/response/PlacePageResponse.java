package com.mitchinmat.domain.place.api.dto.response;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mitchinmat.domain.place.domain.Place;
import com.mitchinmat.global.common.pagination.PageDetail;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PlacePageResponse(
	List<PlaceResponse> placeList,
	int totalPages,
	long totalElements,
	boolean isLast,
	int currPage
) {
	public static PlacePageResponse of(List<Place> places, PageDetail pageDetail) {

		return new PlacePageResponse(
			places.stream().map(PlaceResponse::of).toList(),
			pageDetail.getTotalPages(),
			pageDetail.getTotalElements(),
			pageDetail.isLast(),
			pageDetail.getCurrPage()
		);
	}
}
