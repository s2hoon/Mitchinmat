package com.mitchinmat.domain.friend.application;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.mitchinmat.domain.friend.dao.FriendRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class FriendNetworkService {

	private final FriendRepository friendRepository;

	public Set<Long> getFriendAndFriendOfFriendIds(Long userId) {
		List<Long> friendIds = friendRepository.findActiveFriendIds(userId);
		List<Long> friendsOfFriendsIds = friendRepository.findFriendsOfFriendsByUserId(userId);

		Set<Long> allFriendIds = new HashSet<>();
		allFriendIds.addAll(friendIds);
		allFriendIds.addAll(friendsOfFriendsIds);
		return allFriendIds;
	}

	// 추후에 도메인이 분리되는걸 고려하면, API 로 만들어야하지않을까
	public String[] getFriendAndFriendOfFriendIdsAsString(Long userId) {
		Set<Long> friendOfFriendIds = getFriendAndFriendOfFriendIds(userId);
		return friendOfFriendIds.stream().map(String::valueOf).toArray(String[]::new);
	}

}
