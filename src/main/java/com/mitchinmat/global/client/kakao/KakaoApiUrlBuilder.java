package com.mitchinmat.global.client.kakao;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.springframework.web.util.UriComponentsBuilder;

import com.mitchinmat.global.client.kakao.dto.request.KakaoPlaceRequest;

public class KakaoApiUrlBuilder {

	private static final String URL_PROTOCOL_HTTPS = "https://";
	private static final String KAUTH_KAKAO_COM = "kauth.kakao.com";
	private static final String KAPI_KAKAO_COM = "kapi.kakao.com";
	private static final String DAPI_KAKAO_COM = "dapi.kakao.com";

	/**
	 * @return OAuth2 토큰 요청 URL
	 */
	public static String buildTokenUri() {
		return UriComponentsBuilder.fromHttpUrl(URL_PROTOCOL_HTTPS + KAUTH_KAKAO_COM)
			.path("/oauth/token")
			.toUriString();
	}

	/**
	 * @return 사용자 정보 요청 URL
	 */
	public static String buildUserInfoUri() {
		return UriComponentsBuilder.fromHttpUrl(URL_PROTOCOL_HTTPS + KAPI_KAKAO_COM)
			.path("/v2/user/me")
			.toUriString();
	}

	/**
	 * @return 사용자 연동 해제 URL
	 */
	public static String buildUnlinkUri() {
		return UriComponentsBuilder.fromHttpUrl(URL_PROTOCOL_HTTPS + KAPI_KAKAO_COM)
			.path("/v1/user/unlink")
			.toUriString();
	}

	/**
	 * @return 친구 목록 조회 URL
	 */
	public static String buildFriendsUri(int limit) {
		return UriComponentsBuilder.fromHttpUrl(URL_PROTOCOL_HTTPS + KAPI_KAKAO_COM)
			.path("/v1/api/talk/friends")
			.queryParam("limit", limit)
			.toUriString();
	}

	/**
	 * @return 액세스 토큰 정보 요청 URL
	 */
	public static String buildTokenInfoUri() {
		return UriComponentsBuilder.fromHttpUrl(URL_PROTOCOL_HTTPS + KAPI_KAKAO_COM)
			.path("/v1/user/access_token_info")
			.toUriString();
	}

	/**
	 * @return 사용자 스코프 정보 요청 URL
	 */
	public static String buildScopesUri() {
		return UriComponentsBuilder.fromHttpUrl(URL_PROTOCOL_HTTPS + KAPI_KAKAO_COM)
			.path("/v2/user/scopes")
			.toUriString();
	}

	/**
	 * @param kakaoPlaceRequest 장소 검색 요청 정보
	 * @return 장소 검색 URL
	 */
	public static URI buildSearchUri(KakaoPlaceRequest kakaoPlaceRequest) {
		return UriComponentsBuilder.fromHttpUrl(URL_PROTOCOL_HTTPS + DAPI_KAKAO_COM)
			.path("/v2/local/search/keyword.json")
			.queryParam("category_group_code[]", "FD6")
			.queryParam("category_group_code[]", "CE7")
			.queryParam("query", kakaoPlaceRequest.query())
			.queryParam("size", kakaoPlaceRequest.size())
			.queryParam("page", kakaoPlaceRequest.page())
			.queryParam("x", kakaoPlaceRequest.x())
			.queryParam("y", kakaoPlaceRequest.y())
			.encode(StandardCharsets.UTF_8)
			.build()
			.toUri();
	}

	/**
	 * @param query 검색 쿼리
	 * @param page  페이지 번호
	 * @return 장소 크롤링 URL
	 */
	public static URI buildCrawlingUri(String query, int page) {
		return UriComponentsBuilder.fromHttpUrl(URL_PROTOCOL_HTTPS + DAPI_KAKAO_COM)
			.path("/v2/local/search/keyword.json")
			.queryParam("category_group_code", "FD6")
			.queryParam("query", query)
			.queryParam("size", 15)
			.queryParam("page", page)
			.encode(StandardCharsets.UTF_8)
			.build()
			.toUri();
	}

}
