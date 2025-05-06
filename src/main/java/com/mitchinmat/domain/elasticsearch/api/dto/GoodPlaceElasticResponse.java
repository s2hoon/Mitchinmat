package com.mitchinmat.domain.elasticsearch.api.dto;

import java.util.Set;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoodPlaceElasticResponse(
	Long placeId,
	String placeName,
	String addressName,
	String roadAddressName,
	Set<String> userIds,
	String[] placeCategory,
	double distance,
	GeoPoint location
) {
	public static record GeoPoint(double lat, double lon) {
	}
}