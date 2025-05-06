package com.mitchinmat.domain.place.api.dto.response;

import com.mitchinmat.domain.place.domain.PlaceCrawledData;

public record PlaceCrawledDataResponse(
	int totalKakaoReviews,
	Double kakaoReviewScore,
	String tags,
	String operationTime
) {

	public static PlaceCrawledDataResponse of(PlaceCrawledData placeCrawledData) {
		return new PlaceCrawledDataResponse(
			placeCrawledData.getTotalKakaoReviews(),
			placeCrawledData.getKakaoReviewScore(),
			placeCrawledData.getTags(),
			placeCrawledData.getOperationTime()
		);
	}
}
