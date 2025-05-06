package com.mitchinmat.domain.friend.api.dto.response;

import com.mitchinmat.domain.user.domain.User;

import lombok.Builder;

@Builder
public record FriendDetailResponse(
	long friendId,
	boolean viewStatus,
	String username,
	String profileImage,
	Long goodPlaceCount,
	Long groupCount
) {

	public static FriendDetailResponse of(User friend, boolean viewStatus) {
		return new FriendDetailResponse(
			friend.getId(),
			viewStatus,
			friend.getUsername(),
			friend.getProfileImage(),
			friend.getGoodPlaceCount(),
			friend.getGroupCount()
		);
	}
}
