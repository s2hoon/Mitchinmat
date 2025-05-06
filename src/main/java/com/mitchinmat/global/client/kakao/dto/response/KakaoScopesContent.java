package com.mitchinmat.global.client.kakao.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoScopesContent(
	String id,
	String displayName,
	String type,
	boolean using,
	boolean agreed,
	boolean revocable
) {
}
