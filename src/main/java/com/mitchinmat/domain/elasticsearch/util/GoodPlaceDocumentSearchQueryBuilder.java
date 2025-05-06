package com.mitchinmat.domain.elasticsearch.util;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

public class GoodPlaceDocumentSearchQueryBuilder {

	public static SearchRequest buildSearchRequest(String index, String field, Long value) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.termQuery(field, value));
		return new SearchRequest(index).source(searchSourceBuilder);
	}

	public static SearchRequest buildSearchRequestWithFilters(String index, String query, String[] userIds, double lat,
		double lon) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
			.should(QueryBuilders.matchQuery("combined_field", query))
			.filter(QueryBuilders.termsQuery("user_id", (Object[])userIds))
			.minimumShouldMatch("100%");

		GeoDistanceSortBuilder distanceSort = new GeoDistanceSortBuilder("location", lat, lon)
			.unit(DistanceUnit.KILOMETERS)
			.order(SortOrder.ASC);

		searchSourceBuilder.query(boolQuery)
			.sort("_score", SortOrder.DESC)
			.sort(distanceSort)
			.size(10);

		return new SearchRequest(index).source(searchSourceBuilder);
	}
}
