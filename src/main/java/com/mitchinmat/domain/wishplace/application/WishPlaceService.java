package com.mitchinmat.domain.wishplace.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mitchinmat.domain.place.dao.PlaceRepository;
import com.mitchinmat.domain.place.domain.Place;
import com.mitchinmat.domain.user.dao.UserRepository;
import com.mitchinmat.domain.user.domain.User;
import com.mitchinmat.domain.wishplace.api.dto.response.WishPlaceResponse;
import com.mitchinmat.domain.wishplace.dao.WishPlaceRepository;
import com.mitchinmat.domain.wishplace.domain.WishPlace;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishPlaceService {
	private final WishPlaceRepository wishPlaceRepository;
	private final UserRepository userRepository;
	private final PlaceRepository placeRepository;

	@Transactional
	public void insertWishPlace(Long userId, Long placeId) {
		User user = userRepository.getById(userId);
		Place place = placeRepository.getById(placeId);
		wishPlaceRepository.checkWishPlaceExists(user, place);

		WishPlace wishPlace = WishPlace.builder()
			.user(user)
			.place(place)
			.build();

		wishPlaceRepository.save(wishPlace);
	}

	@Transactional
	public void deleteWishPlace(Long userId, Long wishPlaceId) {
		WishPlace wishPlace = wishPlaceRepository.getById(wishPlaceId);
		wishPlaceRepository.validateWishPlaceOwner(wishPlace, userId);

		wishPlaceRepository.delete(wishPlace);
	}

	public List<WishPlaceResponse> getWishPlaceList(Long userId) {
		List<WishPlace> wishPlaces = wishPlaceRepository.findByUserId(userId);

		return wishPlaces.stream()
			.map(WishPlaceResponse::of)
			.collect(Collectors.toList());
	}

}
