package com.mitchinmat.domain.elasticsearch.dao;

import static com.mitchinmat.domain.elasticsearch.constants.GoodPlaceConstants.*;
import static com.mitchinmat.domain.elasticsearch.util.GoodPlaceDocumentMapper.*;
import static com.mitchinmat.domain.elasticsearch.util.GoodPlaceDocumentSearchQueryBuilder.*;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Repository;

import com.mitchinmat.domain.elasticsearch.domain.GoodPlaceDocument;
import com.mitchinmat.domain.friend.application.FriendNetworkService;
import com.mitchinmat.domain.place.domain.PlaceCategory;
import com.mitchinmat.global.error.ErrorCode;
import com.mitchinmat.global.error.exception.MitchinmatException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class GoodPlaceDocumentRepositoryImpl implements GoodPlaceDocumentRepository {

	private final RestHighLevelClient client;
	private final FriendNetworkService friendNetworkService;

	@Override
	public SearchResponse findGoodPlaceByPlaceId(Long placeId) {
		SearchRequest searchRequest = buildSearchRequest(INDEX_GOODPLACES, COLUMN_PLACE_ID, placeId);
		SearchResponse searchResponse = executeSearchRequest(searchRequest);
		return searchResponse;
	}

	@Override
	public UpdateResponse saveOrUpdateGoodPlace(GoodPlaceDocument goodPlaceDocument) {
		PlaceCategory placeCategory = goodPlaceDocument.getPlaceCategory();
		goodPlaceDocument.setPlaceCategory(placeCategory);
		Map<String, Object> jsonMap = buildJsonMap(goodPlaceDocument);
		UpdateRequest updateRequest = new UpdateRequest(INDEX_GOODPLACES, goodPlaceDocument.getPlaceId().toString())
			.doc(jsonMap, XContentType.JSON)
			.docAsUpsert(true);
		return executeUpdateRequest(updateRequest);
	}

	@Override
	public SearchResponse searchGoodPlace(Long userId, String query, double myLat, double myLon) {
		String[] userIds = friendNetworkService.getFriendAndFriendOfFriendIdsAsString(userId);
		SearchRequest searchRequest = buildSearchRequestWithFilters(INDEX_GOODPLACES, query, userIds, myLat, myLon);
		SearchResponse searchResponse = executeSearchRequest(searchRequest);
		return searchResponse;
	}

	@Override
	public void refreshIndex(String indexName) {
		RefreshRequest request = new RefreshRequest(indexName);
		try {
			client.indices().refresh(request, RequestOptions.DEFAULT);
		} catch (IOException e) {
			throw new MitchinmatException(ErrorCode.ELASTICSEARCH_INDEX_CREATION_FAILED);
		}
	}

	private UpdateResponse executeUpdateRequest(UpdateRequest updateRequest) {
		try {
			return client.update(updateRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			throw new MitchinmatException(ErrorCode.ELASTICSEARCH_CONNECTION_FAILED);
		} catch (Exception e) {
			throw new MitchinmatException(ErrorCode.ELASTICSEARCH_QUERY_EXECUTION_FAILED);
		}
	}

	private SearchResponse executeSearchRequest(SearchRequest searchRequest) {
		try {
			return client.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			throw new MitchinmatException(ErrorCode.ELASTICSEARCH_CONNECTION_FAILED);
		} catch (Exception e) {
			throw new MitchinmatException(ErrorCode.ELASTICSEARCH_QUERY_EXECUTION_FAILED);
		}
	}
}
