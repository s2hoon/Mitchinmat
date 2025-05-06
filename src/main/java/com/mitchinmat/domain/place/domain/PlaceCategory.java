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
public class PlaceCategory {

	private String cateName1;

	private String cateName2;

	private String cateName3;

	private String cateName4;

	private String cateName5;

}
