package com.mitchinmat.domain.friend.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mitchinmat.domain.friend.api.dto.response.FriendDetailListResponse;
import com.mitchinmat.domain.friend.api.dto.response.FriendDetailResponse;
import com.mitchinmat.domain.friend.api.dto.response.FriendOfFriendResponse;
import com.mitchinmat.domain.friend.dao.FriendOfFriendRepository;
import com.mitchinmat.domain.friend.dao.FriendRepository;
import com.mitchinmat.domain.friend.domain.Friend;
import com.mitchinmat.domain.friend.domain.FriendOfFriend;
import com.mitchinmat.domain.user.dao.UserRepository;
import com.mitchinmat.domain.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FriendService {

	private final FriendRepository friendRepository;
	private final FriendOfFriendRepository friendOfFriendRepository;
	private final UserRepository userRepository;
	private final FriendNetworkService friendNetworkService;

	public FriendDetailListResponse getFriends(Long userId) {
		User user = userRepository.getById(userId);
		List<Friend> friends = friendRepository.findAllByUserId(userId);
		List<FriendDetailResponse> friendDetails = new ArrayList<>();
		for (Friend friend : friends) {
			userRepository.findById(friend.getFriendId())
				.ifPresent(friendUser -> friendDetails.add(FriendDetailResponse.of(friendUser, friend.isViewStatus())));
		}
		return FriendDetailListResponse.of(user.getFriendSyncedDate(), friendDetails);
	}

	@Transactional
	public boolean updateFriendViewStatus(Long userId, Long friendId) {
		User user = userRepository.getById(userId);
		User friendUser = userRepository.getById(friendId);
		Friend friend = friendRepository.getByUserIdAndFriendId(user.getId(), friendUser.getId());
		friend.toggleViewStatus();
		return friendRepository.save(friend).isViewStatus();
	}

	@Transactional
	public FriendOfFriendResponse syncFriendOfFriend(Long userId) {
		User user = userRepository.getById(userId);

		Set<Long> allFriendIds = friendNetworkService.getFriendAndFriendOfFriendIds(userId);
		String combinedFriendsString = buildFriendsString(allFriendIds) + userId;

		FriendOfFriend friendOfFriend = friendOfFriendRepository.findById(userId)
			.orElse(new FriendOfFriend(userId, combinedFriendsString));
		friendOfFriend.updateFriendIds(combinedFriendsString);

		user.syncFriendOfFriend(friendOfFriend);
		return FriendOfFriendResponse.of(friendOfFriend);
	}

	private String buildFriendsString(Set<Long> friendIds) {
		if (friendIds.isEmpty()) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		for (Long id : friendIds) {
			sb.append(id).append('%');
		}
		return sb.toString();
	}

}
