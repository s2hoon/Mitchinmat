package com.mitchinmat.domain.elasticsearch.dao;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;

import com.mitchinmat.domain.elasticsearch.domain.GoodPlaceDocument;

public interface GoodPlaceDocumentRepository {
	SearchResponse findGoodPlaceByPlaceId(Long placeId);

	UpdateResponse saveOrUpdateGoodPlace(GoodPlaceDocument goodPlaceDocument);

	SearchResponse searchGoodPlace(Long userId, String query, double myLat, double myLon);

	void refreshIndex(String indexName);
}