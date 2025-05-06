package com.mitchinmat.domain.place.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.mitchinmat.domain.place.api.dto.reqeust.EnrollPlaceRequest;
import com.mitchinmat.domain.place.api.dto.response.PlaceDetailResponse;
import com.mitchinmat.domain.place.dao.PlaceRepository;
import com.mitchinmat.domain.place.domain.Place;
import com.mitchinmat.global.client.kakao.KakaoClient;
import com.mitchinmat.global.client.kakao.dto.request.KakaoPlaceRequest;
import com.mitchinmat.global.client.kakao.dto.response.KakaoPlaceContent;
import com.mitchinmat.global.error.ErrorCode;
import com.mitchinmat.global.error.exception.MitchinmatException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceService {

	private final PlaceRepository placeRepository;
	private final KakaoClient kakaoClient;

	private static final int MAX_PAGES = 45;
	private static final int MIN_SUCCESSFUL_ADDITIONS = 10;

	public PlaceDetailResponse findById(Long placeId) {
		return PlaceDetailResponse.of(placeRepository.findById(placeId).orElse(null));
	}

	@Transactional
	public PlaceDetailResponse addPlace(EnrollPlaceRequest enrollPlaceRequest) {
		validatePlaceDoesNotExist(enrollPlaceRequest.id());
		Place place = enrollPlaceRequest.toEntity();
		placeRepository.save(place);
		return PlaceDetailResponse.of(placeRepository.save(place));
	}

	public void deleteById(Long id) {
		Place place = placeRepository.findById(id)
			.orElseThrow(() -> new MitchinmatException(ErrorCode.PLACE_NOT_FOUND));
		try {
			placeRepository.deleteById(id);
		} catch (Exception e) {
			throw new MitchinmatException(ErrorCode.PLACE_DELETE_FAILED);
		}
	}

	public List<KakaoPlaceContent> callKakaoApi(@RequestBody KakaoPlaceRequest kakaoPlaceRequest) {
		try {
			return kakaoClient.getKakaoSearchResult(kakaoPlaceRequest).documents();
		} catch (Exception e) {
			throw new MitchinmatException(ErrorCode.KAKAO_SERVER_ERROR, e.getMessage());
		}
	}

	public void crawling(String query) {
		for (int page = 1; page <= MAX_PAGES; page++) {
			try {
				List<EnrollPlaceRequest> places = kakaoClient.fetchPlacesFromKakao(query, page);
				int successfulAdditions = addPlacesToDatabase(places);
				if (successfulAdditions < MIN_SUCCESSFUL_ADDITIONS) {
					break;
				}
			} catch (Exception e) {
				throw new MitchinmatException(ErrorCode.KAKAO_SERVER_ERROR);
			}
		}
	}

	private int addPlacesToDatabase(List<EnrollPlaceRequest> places) {
		int successfulAdditions = 0;
		for (EnrollPlaceRequest req : places) {
			try {
				this.addPlace(req);
				successfulAdditions++;
			} catch (Exception e) {
				continue;
			}
		}
		return successfulAdditions;
	}

	private void validatePlaceDoesNotExist(long id) {
		placeRepository.findById(id).ifPresent(value -> {
			throw new MitchinmatException(ErrorCode.PLACE_ALREADY_EXIST);
		});
	}
}
