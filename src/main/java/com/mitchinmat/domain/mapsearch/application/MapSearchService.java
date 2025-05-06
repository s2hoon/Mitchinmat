package com.mitchinmat.domain.mapsearch.application;

import static com.mitchinmat.global.error.ErrorCode.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mitchinmat.domain.friend.dao.FriendOfFriendRepository;
import com.mitchinmat.domain.friend.dao.FriendRepository;
import com.mitchinmat.domain.friend.domain.Friend;
import com.mitchinmat.domain.friend.domain.FriendOfFriend;
import com.mitchinmat.domain.goodplace.dao.GoodPlaceRepository;
import com.mitchinmat.domain.group.dao.GroupRepository;
import com.mitchinmat.domain.group.domain.Group;
import com.mitchinmat.domain.mapsearch.api.dto.request.PlaceSearchRequest;
import com.mitchinmat.domain.mapsearch.api.dto.response.MapSearchResponse;
import com.mitchinmat.domain.mapsearch.api.dto.response.PlaceInfoDetailResponse;
import com.mitchinmat.domain.mapsearch.api.dto.response.PlaceInfoResponse;
import com.mitchinmat.domain.place.api.dto.response.PlaceDetailResponse;
import com.mitchinmat.domain.place.dao.PlaceRepository;
import com.mitchinmat.domain.user.dao.UserRepository;
import com.mitchinmat.domain.user.domain.User;
import com.mitchinmat.domain.wishplace.dao.WishPlaceRepository;
import com.mitchinmat.domain.place.domain.Place;
import com.mitchinmat.domain.wishplace.domain.WishPlace;
import com.mitchinmat.global.error.exception.MitchinmatException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MapSearchService {

	private final PlaceRepository placeRepository;
	private final GoodPlaceRepository goodPlaceRepository;
	private final GroupRepository groupRepository;
	private final UserRepository userRepository;
	private final FriendRepository friendRepository;
	private final FriendOfFriendRepository friendOfFriendRepository;
	private final WishPlaceRepository wishPlaceRepository;

	public MapSearchResponse searchOnMap(
		Long userId,
		PlaceSearchRequest placeSearchRequest) {

		List<Long> friendIds = friendRepository.findFriendIdsByUserId(userId);
		List<Long> friendOfFriendIds = parseFriendOfFriendIds(
			friendOfFriendRepository.findById(userId).orElseThrow(() -> new MitchinmatException(SYNC_NOT_DONE))
		);

		List<Place> myPlaceList = goodPlaceRepository.findGoodPlacesInBounds(userId, placeSearchRequest);
		List<Place> friendPlaceList = goodPlaceRepository.findFriendGoodPlacesInBouds(friendIds, placeSearchRequest);
		List<Place> friendOfFriendPlaceList = goodPlaceRepository.findFriendOfFriendPlacesInBounds(friendOfFriendIds, placeSearchRequest);
		List<WishPlace> wishPlaceList = wishPlaceRepository.findByUserId(userId);

		return MapSearchResponse.of(myPlaceList, friendPlaceList, friendOfFriendPlaceList, wishPlaceList);
	}

	private List<Long> parseFriendOfFriendIds(FriendOfFriend friendOfFriend){
		String[] friendOfFriendIds = friendOfFriend.getFriendIds().split("%");
		return Arrays.stream(friendOfFriendIds).map(Long::parseLong).distinct().collect(Collectors.toList());
	}

	// TO_DO : group place 완료 후 groupList 받아오는거 완성해야함
	public PlaceInfoResponse getPlaceInfo(Long userId, Long placeId) {
		Place place = placeRepository.getById(placeId);

		List<Friend> friendsList = friendRepository.findAllByUserId(userId);
		boolean isGoodPlace = goodPlaceRepository.existsByUserIdAndPlaceId(userId, placeId);
		boolean isWishPlace = wishPlaceRepository.existsByUserIdAndPlaceId(userId, placeId);
		List<User> friendsHavingPlace = userRepository.findUsersByPlaceIdAndFriends(placeId, friendsList);
		List<Group> groupList = groupRepository.findGroupIdsByUserAndPlace(userId, placeId);
		return PlaceInfoResponse.of(place, isGoodPlace, isWishPlace, friendsHavingPlace, groupList);
	}

	// TO_DO : group place 완료 후 groupList 받아오는거 완성해야함
	public PlaceInfoDetailResponse getPlaceInfoDetail(Long userId, Long placeId){
		Place place = placeRepository.getById(placeId);

		List<Friend> friendsList = friendRepository.findAllByUserId(userId);
		boolean isGoodPlace = goodPlaceRepository.existsByUserIdAndPlaceId(userId, placeId);
		boolean isWishPlace = wishPlaceRepository.existsByUserIdAndPlaceId(userId, placeId);
		List<User> friendsHavingPlace = userRepository.findUsersByPlaceIdAndFriends(placeId, friendsList);
		List<Group> groupList = groupRepository.findGroupIdsByUserAndPlace(userId, placeId);
		return PlaceInfoDetailResponse.of(place, isGoodPlace, isWishPlace, friendsHavingPlace, groupList);
	}
}
