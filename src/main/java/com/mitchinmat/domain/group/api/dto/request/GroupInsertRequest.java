package com.mitchinmat.domain.group.api.dto.request;

public record GroupInsertRequest(
	String groupName,
	boolean isPublic
) {
}
