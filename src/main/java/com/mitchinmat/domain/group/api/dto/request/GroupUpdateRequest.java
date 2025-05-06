package com.mitchinmat.domain.group.api.dto.request;

public record GroupUpdateRequest(
	String groupName,
	String description,
	String coverImageUrl
) {
}
