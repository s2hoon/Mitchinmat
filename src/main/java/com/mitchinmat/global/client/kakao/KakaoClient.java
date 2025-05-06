package com.mitchinmat.global.client.kakao;

import static com.mitchinmat.global.error.ErrorCode.*;
import static org.springframework.http.MediaType.*;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.mitchinmat.domain.place.api.dto.reqeust.EnrollPlaceRequest;
import com.mitchinmat.global.client.kakao.config.KakaoAuthProperties;
import com.mitchinmat.global.client.kakao.dto.request.KakaoPlaceRequest;
import com.mitchinmat.global.client.kakao.dto.response.KakaoFriendContent;
import com.mitchinmat.global.client.kakao.dto.response.KakaoFriendsResponse;
import com.mitchinmat.global.client.kakao.dto.response.KakaoOAuthResponse;
import com.mitchinmat.global.client.kakao.dto.response.KakaoPlaceResponse;
import com.mitchinmat.global.client.kakao.dto.response.KakaoScopesContent;
import com.mitchinmat.global.client.kakao.dto.response.KakaoScopesResponse;
import com.mitchinmat.global.client.kakao.dto.response.KakaoUnlinkResponse;
import com.mitchinmat.global.client.kakao.dto.response.KakaoUserInfoResponse;
import com.mitchinmat.global.error.exception.MitchinmatException;
import com.mitchinmat.global.util.RestTemplateUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoClient {

	private final KakaoAuthProperties kakaoAuthProperties;

	public KakaoOAuthResponse reissueToken(String refreshToken) {
		HttpHeaders headers = RestTemplateUtils.createHeaders(APPLICATION_FORM_URLENCODED);

		String tokenUri = KakaoApiUrlBuilder.buildTokenUri();

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "refresh_token");
		body.add("client_id", kakaoAuthProperties.getClientId());
		body.add("client_secret", kakaoAuthProperties.getClientSecret());
		body.add("refresh_token", refreshToken);

		ResponseEntity<KakaoOAuthResponse> response = RestTemplateUtils.sendPostRequest(tokenUri, headers, body,
			KakaoOAuthResponse.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			KakaoOAuthResponse tokenResponse = response.getBody();
			if (tokenResponse != null && tokenResponse.accessToken() != null) {
				return tokenResponse;
			} else {
				throw new MitchinmatException(KAKAO_ACCESS_TOKEN_RETRIEVAL_FAILED);
			}
		} else if (response.getStatusCode().is4xxClientError()) {
			throw new MitchinmatException(KAKAO_BAD_REQUEST, response.getBody() + "");
		} else {
			throw new MitchinmatException(KAKAO_SERVER_ERROR, response.getStatusCode() + "");
		}
	}

	public KakaoUserInfoResponse getUserInfo(String accessToken) {
		HttpHeaders headers = RestTemplateUtils.createHeaders(APPLICATION_FORM_URLENCODED, accessToken);

		String userInfoUri = KakaoApiUrlBuilder.buildUserInfoUri();
		ResponseEntity<KakaoUserInfoResponse> response = RestTemplateUtils.sendGetRequest(userInfoUri, headers,
			KakaoUserInfoResponse.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			KakaoUserInfoResponse kakaoUserInfo = response.getBody();
			if (kakaoUserInfo != null && kakaoUserInfo.id() != null) {
				return kakaoUserInfo;
			} else {
				throw new MitchinmatException(KAKAO_USER_INFO_RETRIEVAL_FAILED);
			}
		} else if (response.getStatusCode().is4xxClientError()) {
			throw new MitchinmatException(KAKAO_BAD_REQUEST, response.getBody() + "");
		} else {
			throw new MitchinmatException(KAKAO_SERVER_ERROR, response.getStatusCode() + "");
		}
	}

	public boolean isAccessTokenValid(String accessToken) {
		HttpHeaders headers = RestTemplateUtils.createHeaders(APPLICATION_FORM_URLENCODED, accessToken);

		String tokenInfoUri = KakaoApiUrlBuilder.buildTokenInfoUri();
		ResponseEntity<String> response = RestTemplateUtils.sendGetRequest(tokenInfoUri, headers, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return true;
		} else {
			return false;
		}
	}

	public Long unlinkUser(String accessToken) {
		HttpHeaders headers = RestTemplateUtils.createHeaders(APPLICATION_FORM_URLENCODED, accessToken);

		String unlinkUri = KakaoApiUrlBuilder.buildUnlinkUri();
		ResponseEntity<KakaoUnlinkResponse> response = RestTemplateUtils.sendPostRequest(unlinkUri, headers, "",
			KakaoUnlinkResponse.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody().id();
		} else {
			throw new MitchinmatException(USER_UNLINK_FAILED);
		}
	}

	public boolean friendsListAgreed(String accessToken) {
		HttpHeaders headers = RestTemplateUtils.createHeaders(APPLICATION_FORM_URLENCODED, accessToken);

		String scopesUri = KakaoApiUrlBuilder.buildScopesUri();
		ResponseEntity<KakaoScopesResponse> response = RestTemplateUtils.sendGetRequest(scopesUri, headers,
			KakaoScopesResponse.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			KakaoScopesResponse kakaoScopesResponse = response.getBody();
			if (kakaoScopesResponse != null) {
				List<KakaoScopesContent> scopes = kakaoScopesResponse.scopes();
				for (KakaoScopesContent scope : scopes) {
					if ("friends".equals(scope.id())) {
						return scope.agreed();
					}
				}
			}
			return false;
		} else if (response.getStatusCode().is4xxClientError()) {
			throw new MitchinmatException(KAKAO_BAD_REQUEST, response.getBody() + "");
		} else {
			throw new MitchinmatException(KAKAO_SERVER_ERROR, response.getStatusCode() + "");
		}
	}

	public List<KakaoFriendContent> getKakaoFriendList(String accessToken) {
		HttpHeaders headers = RestTemplateUtils.createHeaders(APPLICATION_FORM_URLENCODED, accessToken);

		String friendsUri = KakaoApiUrlBuilder.buildFriendsUri(100);
		ResponseEntity<KakaoFriendsResponse> response = RestTemplateUtils.sendGetRequest(friendsUri, headers,
			KakaoFriendsResponse.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody().elements();
		} else {
			throw new MitchinmatException(KAKAO_SYNC_FAILED, response.getStatusCode() + response.toString());
		}
	}

	public KakaoPlaceResponse getKakaoSearchResult(KakaoPlaceRequest kakaoPlaceRequest) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", "KakaoAK " + kakaoAuthProperties.getClientId());

		URI targetUrl = KakaoApiUrlBuilder.buildSearchUri(kakaoPlaceRequest);
		ResponseEntity<KakaoPlaceResponse> result = RestTemplateUtils.sendGetRequestWithUri(targetUrl, httpHeaders,
			KakaoPlaceResponse.class);
		return result.getBody();
	}

	public List<EnrollPlaceRequest> fetchPlacesFromKakao(String query, int page) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", "KakaoAK " + kakaoAuthProperties.getClientId());

		URI targetUrl = KakaoApiUrlBuilder.buildCrawlingUri(query, page);
		ResponseEntity<KakaoPlaceResponse> result = RestTemplateUtils.sendGetRequestWithUri(
			targetUrl, httpHeaders,
			KakaoPlaceResponse.class);
		return result.getBody().documents().stream().map(EnrollPlaceRequest::of).toList();
	}

}
