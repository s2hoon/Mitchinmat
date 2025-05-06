package com.mitchinmat.domain.friend.application;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mitchinmat.domain.friend.dao.FriendRepository;
import com.mitchinmat.domain.friend.domain.Friend;
import com.mitchinmat.domain.user.dao.UserRepository;
import com.mitchinmat.domain.user.domain.User;
import com.mitchinmat.global.client.kakao.KakaoClient;
import com.mitchinmat.global.client.kakao.KakaoTokenManager;
import com.mitchinmat.global.client.kakao.dto.response.KakaoFriendContent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FriendKakaoSyncService {

	private final FriendRepository friendRepository;
	private final UserRepository userRepository;
	private final KakaoClient kakaoClient;
	private final KakaoTokenManager kakaoTokenManager;

	@Transactional
	public void syncFriends(Long userId) {
		User user = userRepository.getById(userId);
		String kakaoAccessToken = kakaoTokenManager.getOrReissueAccessToken(user.getId());
		List<Friend> friends = getNewFriends(user, kakaoAccessToken);
		friendRepository.saveAll(friends);
		user.updateFriendSyncedDate();
	}

	private List<Friend> getNewFriends(User user, String kakaoAccessToken) {
		Set<Long> existingFriendIds = new HashSet<>(friendRepository.findFriendIds(user.getId()));
		List<Friend> allFriends = getRegisteredKakaoFriends(kakaoAccessToken, user);
		return allFriends.stream()
			.filter(friend -> isNewFriend(existingFriendIds, friend, user))
			.peek(friend -> user.getFriends().add(friend))
			.collect(Collectors.toList());
	}

	private List<Friend> getRegisteredKakaoFriends(String kakaoAccessToken, User user) {
		List<KakaoFriendContent> friendContents = kakaoClient.getKakaoFriendList(kakaoAccessToken);
		return friendContents.stream()
			.map(friendContent -> String.valueOf(friendContent.id()))
			.flatMap(oauth2Id -> userRepository.findByOauth2Id(oauth2Id).stream())
			.map(friend -> makeFriend(user, friend))
			.collect(Collectors.toList());
	}

	private boolean isNewFriend(Set<Long> existingFriendIds, Friend friend, User user) {
		return !existingFriendIds.contains(friend.getFriendId()) && !friend.getFriendId().equals(user.getId());
	}

	private Friend makeFriend(User user, User friend) {
		return Friend.builder()
			.userId(user.getId())
			.friendId(friend.getId())
			.viewStatus(true)
			.build();
	}

}
