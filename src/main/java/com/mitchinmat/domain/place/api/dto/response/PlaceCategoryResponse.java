package com.mitchinmat.domain.place.api.dto.response;

import com.mitchinmat.domain.place.domain.PlaceCategory;

public record PlaceCategoryResponse(
	String cateName1,
	String cateName2,
	String cateName3,
	String cateName4,
	String cateName5
) {
	public static PlaceCategoryResponse of(PlaceCategory placeCategory) {
		return new PlaceCategoryResponse(
			placeCategory.getCateName1(),
			placeCategory.getCateName2(),
			placeCategory.getCateName3(),
			placeCategory.getCateName4(),
			placeCategory.getCateName5()
		);
	}
}
