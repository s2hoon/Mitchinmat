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
public class PlaceUrl {

	private String placeUrl;

	private String searchUrl;

	private String roadFindUrl;

	private String imageUrl;

}