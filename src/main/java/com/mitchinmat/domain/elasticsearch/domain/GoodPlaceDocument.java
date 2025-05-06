package com.mitchinmat.domain.elasticsearch.domain;

import java.util.Arrays;
import java.util.Set;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mitchinmat.domain.place.domain.PlaceCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ToString
public class GoodPlaceDocument {

	private Long placeId;
	private Set<String> userIds;
	private String placeName;
	private String addressName;
	private String roadAddressName;
	private PlaceCategory placeCategory;
	private GeoPoint location;

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Setter
	public static class GeoPoint {
		private Double lat;
		private Double lon;
	}
}
