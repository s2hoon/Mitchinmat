package com.mitchinmat.domain.place.api.dto.reqeust;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mitchinmat.domain.place.domain.Place;
import com.mitchinmat.domain.place.domain.PlaceCategory;
import com.mitchinmat.domain.place.domain.PlaceCrawledData;
import com.mitchinmat.domain.place.domain.PlaceUrl;
import com.mitchinmat.global.client.kakao.dto.response.KakaoPlaceContent;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record EnrollPlaceRequest(
	@NotNull(message = "ID는 비워 둘 수 없습니다.")
	long id,

	@NotNull(message = "장소 이름은 비워 둘 수 없습니다.")
	String placeName,

	@NotNull(message = "주소는 비워 둘 수 없습니다.")
	String addressName,

	@NotNull(message = "도로명 주소는 비워 둘 수 없습니다.")
	String roadAddressName,

	@NotNull(message = "전화번호는 비워 둘 수 없습니다.")
	String phone,

	@NotNull(message = "카테고리 이름은 비워 둘 수 없습니다.")
	String categoryName,

	@NotNull(message = "장소 URL은 비워 둘 수 없습니다.")
	String placeUrl,

	@NotNull(message = "경도 (x)는 비워 둘 수 없습니다.")
	@DecimalMin(value = "-180.0", inclusive = true, message = "경도 (x)는 -180과 180 사이여야 합니다.")
	@DecimalMax(value = "180.0", inclusive = true, message = "경도 (x)는 -180과 180 사이여야 합니다.")
	Double x,

	@NotNull(message = "위도 (y)는 비워 둘 수 없습니다.")
	@DecimalMin(value = "-90.0", inclusive = true, message = "위도 (y)는 -90과 90 사이여야 합니다.")
	@DecimalMax(value = "90.0", inclusive = true, message = "위도 (y)는 -90과 90 사이여야 합니다.")
	Double y
) {
	public Place toEntity() {
		PlaceUrl placeUrlParsed = createPlaceUrl();
		PlaceCategory placeCategoryParsed = createPlaceCategory();
		PlaceCrawledData placeCrawledData = createEmptyCrawledData();
		return Place.builder()
			.id(id)
			.placeName(placeName)
			.addressName(addressName)
			.roadAddressName(roadAddressName)
			.phone(phone)
			.placeUrl(placeUrlParsed)
			.placeCategory(placeCategoryParsed)
			.placeCrawledData(placeCrawledData)
			.x(x)
			.y(y)
			.build();
	}

	private PlaceUrl createPlaceUrl() {
		return new PlaceUrl(
			placeUrl,
			getSearchUrl(id, placeName),
			getRouteUrl(id, placeName),
			""
		);
	}

	private PlaceCategory createPlaceCategory(){
		String[] categories = categoryName.split(">");
		return new PlaceCategory(
			categories.length > 0 ? categories[0] : "",
			categories.length > 1 ? categories[1] : "",
			categories.length > 2 ? categories[2] : "",
			categories.length > 3 ? categories[3] : "",
			categories.length > 4 ? categories[4] : ""
		);
	}

	private PlaceCrawledData createEmptyCrawledData(){
		return new PlaceCrawledData(
			0,
			0.0,
			"",
			""
		);
	}

	private String getSearchUrl(long id, String q) {
		return String.format(
			"https://map.kakao.com/?urlLevel=5&itemId=%d&q=%s&srcid=%d&map_type=TYPE_MAP/",
			id, q, id);
	}

	private String getRouteUrl(long id, String q) {
		return String.format(
			"https://map.kakao.com/?map_type=TYPE_MAP&target=car&rt=,,,&rt1=&rt2=%s&rtIds=,%d"
			, q, id);
	}

	public static EnrollPlaceRequest of(KakaoPlaceContent kakaoPlaceContent) {
		return new EnrollPlaceRequest(
			kakaoPlaceContent.id(),
			kakaoPlaceContent.placeName(),
			kakaoPlaceContent.addressName(),
			kakaoPlaceContent.roadAddressName(),
			kakaoPlaceContent.phone(),
			kakaoPlaceContent.categoryName(),
			kakaoPlaceContent.placeUrl(),
			kakaoPlaceContent.x(),
			kakaoPlaceContent.y()
		);
	}
}
