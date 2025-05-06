package com.mitchinmat.domain.place.api;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mitchinmat.domain.place.api.dto.reqeust.EnrollPlaceRequest;
import com.mitchinmat.domain.place.api.dto.response.PlaceDetailResponse;
import com.mitchinmat.domain.place.api.spec.PlaceApiSpecification;
import com.mitchinmat.domain.place.application.PlaceService;
import com.mitchinmat.global.aop.LogExecution;
import com.mitchinmat.global.client.kakao.dto.request.KakaoPlaceRequest;
import com.mitchinmat.global.client.kakao.dto.response.KakaoPlaceContent;
import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.common.response.ResponseUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/place")
@RequiredArgsConstructor
@LogExecution
public class PlaceController implements PlaceApiSpecification {

	private final PlaceService placeService;

	@Override
	@GetMapping("/{placeId}")
	public ApiResponse<PlaceDetailResponse> getPlace(@PathVariable Long placeId) {
		return ResponseUtils.success(placeService.findById(placeId));
	}

	@Override
	@PostMapping
	public ApiResponse<PlaceDetailResponse> addPlace(@Valid @RequestBody EnrollPlaceRequest enrollPlaceRequest) {
		return ResponseUtils.success(placeService.addPlace(enrollPlaceRequest));
	}

	@Override
	@DeleteMapping
	public ApiResponse<Void> deletePlace(@RequestParam Long id) {
		placeService.deleteById(id);
		return ResponseUtils.success();
	}

	@Override
	@PostMapping("/kakao-search")
	public ApiResponse<List<KakaoPlaceContent>> callKakaoApi(@Valid @RequestBody KakaoPlaceRequest kakaoPlaceRequest) {
		return ResponseUtils.success(placeService.callKakaoApi(kakaoPlaceRequest));
	}

	@PostMapping("/kakao-crawling")
	public ApiResponse<Void> kakaoCrawling(@RequestParam String query) {
		placeService.crawling(query);
		return ResponseUtils.success(null);
	}
}
