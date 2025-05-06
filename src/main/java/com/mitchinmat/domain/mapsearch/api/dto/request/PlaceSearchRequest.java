package com.mitchinmat.domain.mapsearch.api.dto.request;

import com.mitchinmat.domain.place.domain.Place;

public record PlaceSearchRequest(
	double top,
	double bottom,
	double left,
	double right,
	String query
) {
	public static PlaceSearchRequest of(Double top, Double bottom, Double left, Double right, String query) {
		return new PlaceSearchRequest(
			(top == null) ? 90 : top,
			(bottom == null) ? 0: bottom,
			(left == null) ? 0 : left,
			(right == null) ? 180 : right,
			(query == null) ? "" : query
		);
	}
}
