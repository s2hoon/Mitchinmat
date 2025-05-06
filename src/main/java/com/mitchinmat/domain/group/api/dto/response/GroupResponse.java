package com.mitchinmat.domain.group.api.dto.response;

import com.mitchinmat.domain.group.domain.Group;

public record GroupResponse(
	long id,
	String name,
	String description,
	String coverImageUrl,
	int placeCount,
	boolean isPublic,
	boolean isCollaborative,
	String viewCode
) {
	// Group -> GroupResponse 변환 메서드 추가
	public static GroupResponse from(Group group) {
		return new GroupResponse(
			group.getId(),
			group.getName(),
			group.getDescription(),
			group.getCoverImageUrl(),
			group.getPlaceCount(),
			group.isPublic(),
			group.isCollaborative(),
			group.getViewCode()
		);
	}
}
