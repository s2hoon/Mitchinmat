package com.mitchinmat.domain.group.api.dto.response;

public record DefaultGroupResponse(
	Long myGroupCount,
	Long goodPlaceCount,
	Boolean goodPlaceIsPublic,
	Long wishPlaceCount
) {
}
