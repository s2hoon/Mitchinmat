package com.mitchinmat.domain.friend.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

@Builder
public record FriendDetailListResponse(
	LocalDateTime syncedDate,
	List<FriendDetailResponse> friends
) {
	public static FriendDetailListResponse of(LocalDateTime syncedDate, List<FriendDetailResponse> friends) {
		return FriendDetailListResponse.builder()
			.syncedDate(syncedDate)
			.friends(friends)
			.build();
	}
}
