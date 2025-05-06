package com.mitchinmat.domain.goodplace.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mitchinmat.domain.friend.dao.FriendRepository;
import com.mitchinmat.domain.goodplace.api.dto.request.GoodPlaceUpdateRequest;
import com.mitchinmat.domain.goodplace.api.dto.response.GoodPlaceResponse;
import com.mitchinmat.domain.goodplace.dao.GoodPlaceRepository;
import com.mitchinmat.domain.goodplace.domain.GoodPlace;
import com.mitchinmat.domain.group.dao.GroupPlaceRepository;
import com.mitchinmat.domain.group.dao.GroupRepository;
import com.mitchinmat.domain.group.domain.Group;
import com.mitchinmat.domain.group.domain.GroupPlace;
import com.mitchinmat.domain.place.dao.PlaceRepository;
import com.mitchinmat.domain.place.domain.Place;
import com.mitchinmat.domain.user.dao.UserRepository;
import com.mitchinmat.domain.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoodPlaceService {

	private final GoodPlaceRepository goodPlaceRepository;
	private final UserRepository userRepository;
	private final PlaceRepository placeRepository;
	private final GroupRepository groupRepository;
	private final GroupPlaceRepository groupPlaceRepository;
	private final FriendRepository friendRepository;

	@Transactional
	public void insertGoodPlace(Long userId, Long placeId) {
		User user = userRepository.getById(userId);
		Place place = placeRepository.getById(placeId);

		goodPlaceRepository.checkDuplicateGoodPlace(userId, placeId);

		GoodPlace goodPlace = GoodPlace.builder()
			.user(user)
			.place(place)
			.publicStatus(true)
			.build();

		goodPlaceRepository.save(goodPlace);
		user.plusGoodPlaceCount();
	}

	@Transactional
	public void deleteGoodPlace(Long userId, Long placeId) {
		User user = userRepository.getById(userId);
		GoodPlace goodPlace = goodPlaceRepository.getByUserIdAndPlaceId(userId, placeId);
		goodPlaceRepository.delete(goodPlace);
		user.minusGoodPlaceCount();
	}

	@Transactional
	public boolean updateStatus(Long userId, Long placeId) {
		User user = userRepository.getById(userId);
		GoodPlace goodPlace = goodPlaceRepository.getByUserIdAndPlaceId(userId, placeId);
		goodPlace.togglePublicStatus();
		if (goodPlace.isPublicStatus()) {
			user.plusGoodPlaceCount();
		} else {
			user.minusGoodPlaceCount();
		}
		return goodPlace.isPublicStatus();
	}

	public List<GoodPlaceResponse> getGoodPlaceListByUserId(Long userId) {
		List<GoodPlace> goodPlaces = goodPlaceRepository.findByUserId(userId);
		return goodPlaces.stream()
			.map(GoodPlaceResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public void updateGoodPlaceGroups(Long userId, Long placeId, GoodPlaceUpdateRequest goodPlaceUpdateRequest) {
		User user = userRepository.getById(userId);
		Place place = placeRepository.getById(placeId);

		if (!goodPlaceRepository.existsByUserIdAndPlaceId(userId, placeId)) {
			insertGoodPlace(userId, placeId);
		}

		List<Group> toAdd = groupRepository.findAllById(goodPlaceUpdateRequest.addGroups());
		List<Group> toDelete = groupRepository.findAllById(goodPlaceUpdateRequest.deleteGroups());

		List<GroupPlace> groupPlaces = new ArrayList<>();
		for (Group group : toAdd) {
			GroupPlace groupPlace = GroupPlace.builder()
				.group(group)
				.place(place)
				.user(user).build();
			group.addGroupPlace(groupPlace);
			groupPlaces.add(groupPlace);
		}

		groupPlaceRepository.saveAll(groupPlaces);

		List<GroupPlace> toRemove = groupPlaceRepository.findByUserAndPlaceAndGroupIn(
			user, place, toDelete
		);
		groupPlaceRepository.deleteAll(toRemove);
	}

	public List<GoodPlaceResponse> getGoodPlacesByFriend(Long userId, Long friendId) {
		friendRepository.getByUserIdAndFriendId(userId, friendId);
		List<GoodPlace> goodPlaces = goodPlaceRepository.findByUserIdAndPublicStatusTrue(friendId);
		return goodPlaces.stream()
			.map(GoodPlaceResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public boolean updateGoodPlacesStatus(Long userId) {
		User user = userRepository.getById(userId);
		user.toggleGoodPlacesPublicStatus();
		return userRepository.save(user).isGoodPlacesPublicStatus();
	}
}
