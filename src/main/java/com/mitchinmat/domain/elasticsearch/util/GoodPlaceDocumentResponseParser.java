package com.mitchinmat.domain.elasticsearch.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.mitchinmat.domain.elasticsearch.api.dto.GoodPlaceElasticResponse;
import com.mitchinmat.domain.elasticsearch.domain.GoodPlaceDocument;
import com.mitchinmat.domain.place.domain.PlaceCategory;

public class GoodPlaceDocumentResponseParser {
	public static GoodPlaceDocument buildGoodPlaceDocument(Map<String, Object> sourceAsMap) {
		GoodPlaceDocument document = new GoodPlaceDocument();
		document.setPlaceId(Long.valueOf(sourceAsMap.get("place_id").toString()));
		document.setPlaceName(sourceAsMap.get("place_name").toString());
		document.setAddressName(sourceAsMap.get("address_name").toString());
		document.setRoadAddressName(sourceAsMap.get("road_address_name").toString());

		List<String> userIds = (List<String>)sourceAsMap.get("user_id");
		document.setUserIds(userIds != null ? new HashSet<>(userIds) : new HashSet<>());

		PlaceCategory placeCategory = (PlaceCategory)sourceAsMap.get("place_category");
		document.setPlaceCategory(placeCategory);

		Map<String, Object> locationMap = (Map<String, Object>)sourceAsMap.get("location");
		document.setLocation(
			new GoodPlaceDocument.GeoPoint((Double)locationMap.get("lat"), (Double)locationMap.get("lon")));

		return document;
	}

	public static GoodPlaceDocument parseSearchResponse(SearchResponse searchResponse) {
		SearchHits hits = searchResponse.getHits();
		if (hits.getTotalHits().value > 0) {
			SearchHit hit = hits.getAt(0);
			return buildGoodPlaceDocument(hit.getSourceAsMap());
		}
		return null;
	}

	public static List<GoodPlaceElasticResponse> parseSearchResults(SearchResponse searchResponse, double myLat,
		double myLon) {
		List<GoodPlaceElasticResponse> results = new ArrayList<>();
		for (SearchHit hit : searchResponse.getHits()) {
			Map<String, Object> sourceAsMap = hit.getSourceAsMap();
			Double distance = (Double)hit.getSortValues()[1];
			results.add(buildGoodPlaceElasticResponse(sourceAsMap, distance));
		}
		return results;
	}

	public static GoodPlaceElasticResponse buildGoodPlaceElasticResponse(Map<String, Object> sourceAsMap,
		double distance) {
		Long placeId = Long.valueOf(sourceAsMap.get("place_id").toString());
		String placeName = sourceAsMap.get("place_name").toString();
		String addressName = sourceAsMap.get("address_name").toString();
		String roadAddressName = sourceAsMap.get("road_address_name").toString();
		Set<String> userIds = sourceAsMap.get("user_id") != null
			? new HashSet<>((List<String>)sourceAsMap.get("user_id"))
			: new HashSet<>();
		String[] placeCategory = ((List<String>)sourceAsMap.get("place_category")).toArray(new String[0]);
		Map<String, Object> locationMap = (Map<String, Object>)sourceAsMap.get("location");
		double lat = (Double)locationMap.get("lat");
		double lon = (Double)locationMap.get("lon");
		GoodPlaceElasticResponse.GeoPoint location = new GoodPlaceElasticResponse.GeoPoint(lat, lon);

		return new GoodPlaceElasticResponse(placeId, placeName, addressName, roadAddressName, userIds, placeCategory,
			distance, location);
	}
}
