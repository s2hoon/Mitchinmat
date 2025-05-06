package com.mitchinmat.global.client.kakao.dto.response;

import java.util.List;

public record KakaoPlaceResponse(

	List<KakaoPlaceContent> documents
) {
}
