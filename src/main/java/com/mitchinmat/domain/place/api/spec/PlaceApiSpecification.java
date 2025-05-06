package com.mitchinmat.domain.place.api.spec;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.mitchinmat.domain.place.api.dto.reqeust.EnrollPlaceRequest;
import com.mitchinmat.domain.place.api.dto.response.PlaceDetailResponse;
import com.mitchinmat.global.client.kakao.dto.request.KakaoPlaceRequest;
import com.mitchinmat.global.client.kakao.dto.response.KakaoPlaceContent;
import com.mitchinmat.global.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Place API", description = "장소 관련 API")
public interface PlaceApiSpecification {

	@Operation(summary = "장소 세부 조회", description = "장소 ID로 장소의 세부 정보를 조회합니다.")
	ApiResponse<PlaceDetailResponse> getPlace(@PathVariable Long placeId);

	@Operation(summary = "장소 추가", description = "새로운 장소를 추가합니다.")
	ApiResponse<PlaceDetailResponse> addPlace(@Valid @RequestBody EnrollPlaceRequest enrollPlaceRequest);

	@Operation(summary = "장소 삭제", description = "장소 ID로 장소를 삭제합니다.")
	ApiResponse<Void> deletePlace(@RequestParam Long id);

	@Operation(summary = "카카오 검색 리스트 조회", description = "카카오 API를 호출하여 장소 검색 결과를 조회합니다.")
	ApiResponse<List<KakaoPlaceContent>> callKakaoApi(
		@Valid @RequestBody KakaoPlaceRequest kakaoPlaceRequest);
}
