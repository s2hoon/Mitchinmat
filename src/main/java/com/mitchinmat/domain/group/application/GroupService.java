package com.mitchinmat.domain.group.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mitchinmat.domain.friend.dao.FriendRepository;
import com.mitchinmat.domain.friend.domain.Friend;
import com.mitchinmat.domain.goodplace.dao.GoodPlaceRepository;
import com.mitchinmat.domain.goodplace.domain.GoodPlace;
import com.mitchinmat.domain.group.api.dto.request.GroupInsertRequest;
import com.mitchinmat.domain.group.api.dto.request.GroupUpdateRequest;
import com.mitchinmat.domain.group.api.dto.response.DefaultGroupResponse;
import com.mitchinmat.domain.group.api.dto.response.FriendGroupListResponse;
import com.mitchinmat.domain.group.api.dto.response.GroupDetailResponse;
import com.mitchinmat.domain.group.api.dto.response.GroupListResponse;
import com.mitchinmat.domain.group.api.dto.response.GroupPlaceResponse;
import com.mitchinmat.domain.group.api.dto.response.GroupResponse;
import com.mitchinmat.domain.group.api.dto.response.ViewCodeResponse;
import com.mitchinmat.domain.group.dao.GroupRepository;
import com.mitchinmat.domain.group.dao.SubscribeGroupRepository;
import com.mitchinmat.domain.group.domain.Group;
import com.mitchinmat.domain.group.domain.GroupPlace;
import com.mitchinmat.domain.group.domain.SubscribeGroup;
import com.mitchinmat.domain.groupmember.dao.GroupMemberRepository;
import com.mitchinmat.domain.groupmember.domain.GroupMember;
import com.mitchinmat.domain.place.api.dto.response.PlaceDetailResponse;
import com.mitchinmat.domain.user.dao.UserRepository;
import com.mitchinmat.domain.user.domain.User;
import com.mitchinmat.domain.wishplace.dao.WishPlaceRepository;
import com.mitchinmat.domain.wishplace.domain.WishPlace;
import com.mitchinmat.global.error.ErrorCode;
import com.mitchinmat.global.error.exception.MitchinmatException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {

	private final GroupRepository groupRepository;
	private final UserRepository userRepository;
	private final GoodPlaceRepository goodPlaceRepository;
	private final WishPlaceRepository wishPlaceRepository;
	private final FriendRepository friendRepository;
	private final GroupMemberRepository groupMemberRepository;
	private final SubscribeGroupRepository subscribeGroupRepository;

	@Transactional
	public GroupResponse insertGroup(Long userId, GroupInsertRequest groupInsertRequest) {
		Group group = Group.builder()
			.ownerUserId(userId)
			.name(groupInsertRequest.groupName())
			.isPublic(groupInsertRequest.isPublic())
			.members(new ArrayList<>())
			.places(new ArrayList<>())
			.subscribeGroups(new ArrayList<>())
			.build();
		group.setViewCode();
		group = groupRepository.save(group);
		User user = userRepository.getById(userId);
		GroupMember groupMember = GroupMember.builder()
			.group(group)
			.user(user)
			.build();
		groupMemberRepository.save(groupMember);
		group.addMember(groupMember);
		return GroupResponse.from(group);
	}

	@Transactional
	public GroupResponse updateGroup(Long userId, Long groupId, GroupUpdateRequest request) {
		Group group = groupRepository.getByIdOrThrow(groupId);
		validateGroupOwner(userId, group);
		group.updateGroupInfo(request.groupName(), request.description(), request.coverImageUrl());
		groupRepository.save(group);
		return GroupResponse.from(group);
	}

	public GroupListResponse getMyGroupList(Long userId) {
		List<Group> myGroup = groupRepository.findByOwnerUserId(userId);
		long myGroupCount = myGroup.size();
		List<GoodPlace> goodPlaces = goodPlaceRepository.findByUserId(userId);
		long goodPlaceCount = goodPlaces.size();
		List<WishPlace> wishPlaces = wishPlaceRepository.findByUserId(userId);
		long wishPlaceCount = wishPlaces.size();
		DefaultGroupResponse defaultGroupResponse = new DefaultGroupResponse(myGroupCount, goodPlaceCount, true,
			wishPlaceCount);
		List<GroupResponse> myGroups = myGroup.stream()
			.filter(group -> !group.isCollaborative())
			.map(GroupResponse::from)
			.collect(Collectors.toList());
		List<GroupResponse> collaborativeGroups = myGroup.stream()
			.filter(Group::isCollaborative)
			.map(GroupResponse::from)
			.collect(Collectors.toList());
		List<SubscribeGroup> subscribeGroupList = subscribeGroupRepository.findByUserId(userId);
		List<GroupResponse> subscribedGroups = subscribeGroupList.stream()
			.map(SubscribeGroup::getGroup)
			.map(GroupResponse::from)
			.collect(Collectors.toList());
		GroupListResponse groupListResponse = new GroupListResponse(defaultGroupResponse, myGroups, collaborativeGroups,
			subscribedGroups);
		return groupListResponse;
	}

	@Transactional
	public GroupResponse copyGroup(Long userId, Long groupId) {
		Group originalGroup = groupRepository.getByIdOrThrow(groupId);
		Group copiedGroup = groupRepository.save(
			Group.builder()
				.ownerUserId(userId)
				.name(originalGroup.getName() + " - 복사본")
				.description(originalGroup.getDescription())
				.coverImageUrl(originalGroup.getCoverImageUrl())
				.placeCount(originalGroup.getPlaceCount())
				.isPublic(true)
				.isCollaborative(false)
				.members(new ArrayList<>())
				.places(new ArrayList<>())
				.subscribeGroups(new ArrayList<>())
				.build()
		);
		User user = userRepository.getById(userId);
		List<GroupPlace> copiedPlaces = originalGroup.getPlaces().stream()
			.map(originalPlace -> GroupPlace.builder()
				.group(copiedGroup)
				.user(user)
				.place(originalPlace.getPlace())
				.build()
			)
			.collect(Collectors.toList());
		copiedGroup.getPlaces().addAll(copiedPlaces);
		GroupMember groupMember = GroupMember.builder()
			.group(copiedGroup)
			.user(user)
			.build();
		groupMemberRepository.save(groupMember);
		copiedGroup.addMember(groupMember);
		return GroupResponse.from(copiedGroup);
	}

	@Transactional
	public ViewCodeResponse getViewCode(Long userId, Long groupId) {
		Group group = groupRepository.getByIdOrThrow(groupId);
		groupMemberRepository.existsByGroupIdAndUserId(groupId, userId);
		return new ViewCodeResponse(groupId, group.getViewCode());
	}

	@Transactional
	public void deleteGroup(Long userId, Long groupId) {
		Group group = groupRepository.getByIdOrThrow(groupId);
		validateGroupOwner(userId, group);
		groupRepository.delete(group);
	}

	public FriendGroupListResponse getFriendGroupList(Long userId, Long friendId) {
		List<Group> friendGroup = groupRepository.findByOwnerUserId(friendId);
		List<GroupResponse> groupList = friendGroup.stream()
			.filter(Group::isPublic)
			.map(GroupResponse::from)
			.collect(Collectors.toList());
		Friend friend = friendRepository.getByUserIdAndFriendId(userId, friendId);
		return new FriendGroupListResponse(groupList.size(), friend.isViewStatus(), groupList);
	}

	public GroupDetailResponse getGroupDetail(Long groupId) {
		Group groups = groupRepository.getByIdOrThrow(groupId);
		GroupResponse group = GroupResponse.from(groups);
		List<GroupPlaceResponse> groupPlaceList = groups.getPlaces().stream()
			.map(groupPlace -> new GroupPlaceResponse(
				groupPlace.getGroup().getId(),
				groupPlace.getGroup().getOwnerUserId(),
				PlaceDetailResponse.of(groupPlace.getPlace())
			))
			.collect(Collectors.toList());
		return new GroupDetailResponse(group, groupPlaceList);
	}

	@Transactional
	public Boolean updateGroupStatus(Long userId, Long groupId) {
		Group group = groupRepository.getByIdOrThrow(groupId);
		validateGroupOwner(userId, group);
		return group.togglePublicStatus();
	}

	private void validateGroupOwner(Long userId, Group group) {
		if (!group.getOwnerUserId().equals(userId)) {
			throw new MitchinmatException(ErrorCode.GROUP_PERMISSION_REFUSED);
		}
	}
}
