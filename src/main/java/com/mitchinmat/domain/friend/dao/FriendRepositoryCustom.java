package com.mitchinmat.domain.friend.dao;

import java.util.List;

public interface FriendRepositoryCustom {

	List<Long> findFriendIdsByUserId(Long userId);

	List<Long> findFriendsOfFriendsByUserId(Long userId);

}
