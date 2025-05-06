package com.mitchinmat.domain.group.api.dto.response;

import java.util.List;

public record GroupListResponse(
	DefaultGroupResponse defaultGroupResponse,
	List<GroupResponse> myGroups,
	List<GroupResponse> collaborativeGroups,
	List<GroupResponse> subscribedGroups
) {
}
