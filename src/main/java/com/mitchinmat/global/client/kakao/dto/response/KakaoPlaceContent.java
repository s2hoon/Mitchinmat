package com.mitchinmat.global.client.kakao.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoPlaceContent(

	long id,
	String placeName,
	String addressName,
	String roadAddressName,
	String phone,
	String categoryName,
	String placeUrl,
	Double x,
	Double y
) {
}
