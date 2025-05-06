package com.mitchinmat.domain.friend.api.dto.response;

import com.mitchinmat.domain.friend.domain.FriendOfFriend;

public record FriendOfFriendResponse(
	Long userId,
	String friendOfFriend
) {

	public static FriendOfFriendResponse of(FriendOfFriend fof) {
		return new FriendOfFriendResponse(
			fof.getUserId(),
			fof.getFriendIds()
		);
	}
}
