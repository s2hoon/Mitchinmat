package com.mitchinmat.domain.group.api.dto.response;

import java.util.List;

public record FriendGroupListResponse(
	long groupCount,
	boolean viewStatus,
	List<GroupResponse> groupList
) {
}
