package com.mitchinmat.domain.elasticsearch.api.spec;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

import com.mitchinmat.global.security.oauth2.model.CustomOAuth2User;
import com.mitchinmat.global.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "ElasticSearch API", description = "맛집 검색 자동완성 API")
public interface ElasticSearchApiSpecification {

	@Operation(summary = "내 친구들의 맛집 리스트 자동완성 검색", description = "내 친구들의 맛집 중에서 자동완성 검색")
	public ApiResponse<?> searchGoodPlaces(
		@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
		@PathVariable String query,
		@PathVariable double myLat,
		@PathVariable double myLon
	);

}
