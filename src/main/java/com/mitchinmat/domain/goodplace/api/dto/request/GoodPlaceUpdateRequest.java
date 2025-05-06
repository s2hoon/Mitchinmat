package com.mitchinmat.domain.goodplace.api.dto.request;

import java.util.List;

public record GoodPlaceUpdateRequest(
	List<Long> addGroups,
	List<Long> deleteGroups
) {
}
