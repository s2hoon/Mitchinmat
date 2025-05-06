package com.mitchinmat.domain.place.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceCrawledData {

	private int totalKakaoReviews;

	private Double kakaoReviewScore;

	private String tags;

	private String operationTime;

}
