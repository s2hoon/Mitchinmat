package com.mitchinmat.global.client.kakao.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoUserInfoResponse(
	Long id,
	String connectedAt,
	KakaoAccount kakaoAccount
) {
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public record KakaoAccount(
		Profile profile
	) {
		@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
		public record Profile(
			String nickname,
			String profileImageUrl
		) {
		}
	}
}
