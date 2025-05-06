package com.mitchinmat.domain.group.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mitchinmat.domain.group.dao.GroupPlaceRepository;
import com.mitchinmat.domain.group.dao.GroupRepository;
import com.mitchinmat.domain.group.domain.Group;
import com.mitchinmat.domain.group.domain.GroupPlace;
import com.mitchinmat.domain.groupmember.dao.GroupMemberRepository;
import com.mitchinmat.domain.place.dao.PlaceRepository;
import com.mitchinmat.domain.place.domain.Place;
import com.mitchinmat.domain.user.dao.UserRepository;
import com.mitchinmat.domain.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupPlaceService {

	private final GroupPlaceRepository groupPlaceRepository;
	private final GroupRepository groupRepository;
	private final PlaceRepository placeRepository;
	private final UserRepository userRepository;
	private final GroupMemberRepository groupMemberRepository;

	@Transactional
	public void addPlacesToGroup(Long userId, Long groupId, List<Long> placeIds) {
		Group group = groupRepository.getByIdOrThrow(groupId);
		User user = userRepository.getById(userId);
		groupMemberRepository.getByGroupIdAndUserId(groupId, userId);
		for (Long placeId : placeIds) {
			Place place = placeRepository.getById(placeId);
			GroupPlace groupPlace = GroupPlace.builder()
				.group(group)
				.place(place)
				.user(user)
				.build();
			group.addGroupPlace(groupPlace);
			groupPlaceRepository.save(groupPlace);
		}
	}
	@Transactional
	public void removePlaceFromGroup(Long userId, Long groupId, Long placeId) {
		Group group = groupRepository.getByIdOrThrow(groupId);
		userRepository.getById(userId);
		groupMemberRepository.getByGroupIdAndUserId(groupId, userId);
		GroupPlace groupPlace = groupPlaceRepository.getByGroupIdAndPlaceIdOrThrow(groupId, placeId);
		group.getPlaces().remove(groupPlace);
		groupPlaceRepository.delete(groupPlace);
	}


}
