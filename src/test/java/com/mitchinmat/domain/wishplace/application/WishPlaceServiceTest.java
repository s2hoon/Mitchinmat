package com.mitchinmat.domain.wishplace.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mitchinmat.domain.place.dao.PlaceRepository;
import com.mitchinmat.domain.place.domain.Place;
import com.mitchinmat.domain.place.domain.PlaceCrawledData;
import com.mitchinmat.domain.user.dao.UserRepository;
import com.mitchinmat.domain.user.domain.User;
import com.mitchinmat.domain.wishplace.api.dto.response.WishPlaceResponse;
import com.mitchinmat.domain.wishplace.dao.WishPlaceRepository;
import com.mitchinmat.domain.wishplace.domain.WishPlace;
import com.mitchinmat.global.error.ErrorCode;
import com.mitchinmat.global.error.exception.MitchinmatException;


@ExtendWith(MockitoExtension.class)
class WishPlaceServiceTest {

	@Mock
	private WishPlaceRepository wishPlaceRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PlaceRepository placeRepository;

	@InjectMocks
	private WishPlaceService wishPlaceService;

	private User user;
	private Place place;
	private WishPlace wishPlace;

	@BeforeEach
	void setUp() throws Exception {
		user = User.createOAuthUserBuilder().build();
		setField(user, "id", 1L);
		PlaceCrawledData placeCrawledData = PlaceCrawledData.builder()
			.tags("맛집, 한식") // 테스트 데이터 추가
			.build();
		place = Place.builder()
			.id(1L)
			.addressName("서울 강남구")
			.placeName("맛있는 한식집")
			.placeCrawledData(placeCrawledData) // PlaceCrawledData 설정
			.build();
		wishPlace = WishPlace.builder().id(1L).user(user).place(place).build();
	}

	private void setField(Object target, String fieldName, Object value) throws Exception {
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	@Test
	void shouldInsertWishPlaceSuccessfully() {
		// Given
		Long userId = user.getId();
		Long placeId = place.getId();

		when(userRepository.getById(userId)).thenReturn(user);
		when(placeRepository.getById(placeId)).thenReturn(place);
		doNothing().when(wishPlaceRepository).checkWishPlaceExists(user, place);

		// When
		wishPlaceService.insertWishPlace(userId, placeId);

		// Then
		verify(wishPlaceRepository, times(1)).save(any(WishPlace.class));
	}

	@Test
	void shouldThrowExceptionWhenWishPlaceAlreadyExists() {
		// Given
		Long userId = user.getId();
		Long placeId = place.getId();

		when(userRepository.getById(userId)).thenReturn(user);
		when(placeRepository.getById(placeId)).thenReturn(place);
		doThrow(new MitchinmatException(ErrorCode.WISH_PLACE_ALREADY_EXISTS))
			.when(wishPlaceRepository).checkWishPlaceExists(user, place);

		// When & Then
		MitchinmatException exception = assertThrows(MitchinmatException.class, () ->
			wishPlaceService.insertWishPlace(userId, placeId)
		);

		assertEquals(ErrorCode.WISH_PLACE_ALREADY_EXISTS.getMessage(), exception.getMessage());
		verify(wishPlaceRepository, never()).save(any(WishPlace.class));
	}

	@Test
	void shouldDeleteWishPlaceSuccessfully() {
		// Given
		Long userId = user.getId();
		Long wishPlaceId = wishPlace.getId();

		when(wishPlaceRepository.getById(wishPlaceId)).thenReturn(wishPlace);
		doNothing().when(wishPlaceRepository).validateWishPlaceOwner(wishPlace, userId);

		// When
		wishPlaceService.deleteWishPlace(userId, wishPlaceId);

		// Then
		verify(wishPlaceRepository, times(1)).delete(wishPlace);
	}

	@Test
	void shouldThrowExceptionWhenWishPlaceNotFound() {
		// Given
		Long userId = user.getId();
		Long wishPlaceId = 999L;

		when(wishPlaceRepository.getById(wishPlaceId))
			.thenThrow(new MitchinmatException(ErrorCode.WISH_PLACE_NOT_FOUND));

		// When & Then
		MitchinmatException exception = assertThrows(MitchinmatException.class, () ->
			wishPlaceService.deleteWishPlace(userId, wishPlaceId)
		);

		assertEquals(ErrorCode.WISH_PLACE_NOT_FOUND.getMessage(), exception.getMessage());
		verify(wishPlaceRepository, never()).delete(any(WishPlace.class));
	}

	@Test
	void shouldThrowExceptionWhenUserIsNotOwner() {
		// Given
		Long userId = 2L; // 다른 사용자 ID
		Long wishPlaceId = wishPlace.getId();

		when(wishPlaceRepository.getById(wishPlaceId)).thenReturn(wishPlace);
		doThrow(new MitchinmatException(ErrorCode.WISH_PLACE_FORBIDDEN))
			.when(wishPlaceRepository).validateWishPlaceOwner(wishPlace, userId);

		// When & Then
		MitchinmatException exception = assertThrows(MitchinmatException.class, () ->
			wishPlaceService.deleteWishPlace(userId, wishPlaceId)
		);

		assertEquals(ErrorCode.WISH_PLACE_FORBIDDEN.getMessage(), exception.getMessage());
		verify(wishPlaceRepository, never()).delete(any(WishPlace.class));
	}

	@Test
	void shouldReturnWishPlaceListSuccessfully() {
		// Given
		Long userId = user.getId();
		when(wishPlaceRepository.findByUserId(userId)).thenReturn(List.of(wishPlace));

		// When
		List<WishPlaceResponse> result = wishPlaceService.getWishPlaceList(userId);

		// Then
		assertNotNull(result);
		assertEquals(1, result.size());
		WishPlaceResponse response = result.get(0);
		assertEquals(wishPlace.getId(), response.wishPlaceId());
		assertEquals(place.getAddressName(), response.addressName());
		assertEquals(place.getPlaceName(), response.placeName());

		verify(wishPlaceRepository, times(1)).findByUserId(userId);
	}

	@Test
	void shouldReturnEmptyWishPlaceList() {
		// Given
		Long userId = user.getId();
		when(wishPlaceRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

		// When
		List<WishPlaceResponse> result = wishPlaceService.getWishPlaceList(userId);

		// Then
		assertNotNull(result);
		assertTrue(result.isEmpty());

		verify(wishPlaceRepository, times(1)).findByUserId(userId);
	}
}