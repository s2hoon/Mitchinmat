package com.mitchinmat.domain.elasticsearch.application;

import static com.mitchinmat.domain.elasticsearch.util.GoodPlaceDocumentResponseParser.*;

import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.springframework.stereotype.Service;

import com.mitchinmat.domain.elasticsearch.api.dto.GoodPlaceElasticResponse;
import com.mitchinmat.domain.elasticsearch.dao.GoodPlaceDocumentRepository;
import com.mitchinmat.domain.elasticsearch.domain.GoodPlaceDocument;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoodPlaceElasticSearchService {

	private final GoodPlaceDocumentRepository goodPlaceDocumentRepository;

	public GoodPlaceDocument findGoodPlaceByPlaceId(Long placeId) {
		SearchResponse searchResponse = goodPlaceDocumentRepository.findGoodPlaceByPlaceId(placeId);
		return parseSearchResponse(searchResponse);
	}

	public UpdateResponse saveOrUpdateGoodPlace(GoodPlaceDocument goodPlaceDocument) {
		return goodPlaceDocumentRepository.saveOrUpdateGoodPlace(goodPlaceDocument);
	}

	public List<GoodPlaceElasticResponse> searchGoodPlace(Long userId, String query, double myLat, double myLon) {
		SearchResponse searchResponse = goodPlaceDocumentRepository.searchGoodPlace(userId, query, myLat, myLon);
		return parseSearchResults(searchResponse, myLat, myLon);
	}

	public void refreshIndex(String indexName) {
		goodPlaceDocumentRepository.refreshIndex(indexName);
	}
}