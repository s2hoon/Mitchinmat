package com.mitchinmat.domain.elasticsearch.util;

import static com.mitchinmat.domain.elasticsearch.constants.GoodPlaceConstants.*;

import java.util.HashMap;
import java.util.Map;

import com.mitchinmat.domain.elasticsearch.domain.GoodPlaceDocument;

public class GoodPlaceDocumentMapper {

	public static Map<String, Object> buildJsonMap(GoodPlaceDocument goodPlaceDocument) {
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put(COLUMN_PLACE_ID, goodPlaceDocument.getPlaceId());
		jsonMap.put(COLUMN_USER_ID, goodPlaceDocument.getUserIds().toArray(new String[0]));
		jsonMap.put(COLUMN_PLACE_NAME, goodPlaceDocument.getPlaceName());
		jsonMap.put(COLUMN_ADDRESS_NAME, goodPlaceDocument.getAddressName());
		jsonMap.put(COLUMN_ROAD_ADDRESS_NAME, goodPlaceDocument.getRoadAddressName());
		jsonMap.put(COLUMN_PLACE_CATEGORY, goodPlaceDocument.getPlaceCategory());

		Map<String, Object> locationMap = new HashMap<>();
		locationMap.put(LOCATION_LAT, goodPlaceDocument.getLocation().getLat());
		locationMap.put(LOCATION_LON, goodPlaceDocument.getLocation().getLon());
		jsonMap.put(COLUMN_LOCATION, locationMap);

		return jsonMap;
	}
}
