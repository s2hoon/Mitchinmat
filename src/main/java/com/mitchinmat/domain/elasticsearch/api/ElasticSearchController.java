package com.mitchinmat.domain.elasticsearch.api;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mitchinmat.domain.elasticsearch.api.dto.GoodPlaceElasticResponse;
import com.mitchinmat.domain.elasticsearch.api.spec.ElasticSearchApiSpecification;
import com.mitchinmat.domain.elasticsearch.application.GoodPlaceElasticSearchService;
import com.mitchinmat.global.aop.LogExecution;
import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;
import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.common.response.ResponseUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/elastic-search/")
@LogExecution
public class ElasticSearchController implements ElasticSearchApiSpecification {

	private final GoodPlaceElasticSearchService goodPlaceElasticSearchService;

	/** url 변경으로 인해 삭제 예정 **/
	@GetMapping("/query/{query}/{myLat}/{myLon}")
	public ApiResponse<List<GoodPlaceElasticResponse>> searchGoodPlacesOldV(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable String query,
		@PathVariable double myLat,
		@PathVariable double myLon
	) {

		Long userId = customOAuth2User.getUserId();
		List<GoodPlaceElasticResponse> goodPlaceDocumentList = goodPlaceElasticSearchService.searchGoodPlace(userId,
			query, myLat, myLon);
		return ResponseUtils.success(goodPlaceDocumentList);
	}

	@GetMapping("/query/{query}/lat/{myLat}/lon/{myLon}")
	public ApiResponse<List<GoodPlaceElasticResponse>> searchGoodPlaces(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable String query,
		@PathVariable double myLat,
		@PathVariable double myLon
	) {
		Long userId = customOAuth2User.getUserId();
		List<GoodPlaceElasticResponse> goodPlaceDocumentList = goodPlaceElasticSearchService.searchGoodPlace(
			userId, query, myLat, myLon);
		return ResponseUtils.success(goodPlaceDocumentList);
	}

}
