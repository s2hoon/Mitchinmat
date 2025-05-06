package com.mitchinmat.domain.group.api.dto.response;

import java.util.List;

public record GroupDetailResponse(
	GroupResponse group,
	List<GroupPlaceResponse> groupPlaceList
) {
}
