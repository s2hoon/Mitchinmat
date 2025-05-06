package com.mitchinmat.domain.groupmember.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import com.mitchinmat.domain.group.domain.Group;

import lombok.Builder;

@Builder
public record GroupInvitationCode(
	String code,
	Long groupId,
	String groupName,
	String groupDescription,
	String groupCoverImageUrl,
	Integer placeCount,
	LocalDateTime createdTime,
	LocalDateTime expirationTime
) {

	private static final long validityDays = 7;

	public static GroupInvitationCode create(Group group) {
		LocalDateTime now = LocalDateTime.now();
		return GroupInvitationCode.builder()
			.code(generateCode())
			.groupId(group.getId())
			.groupName(group.getName())
			.groupDescription(group.getDescription())
			.groupCoverImageUrl(group.getCoverImageUrl())
			.placeCount(group.getPlaceCount())
			.createdTime(now)
			.expirationTime(now.plusDays(validityDays))
			.build();
	}

	private static String generateCode() {
		return UUID.randomUUID().toString().substring(0, 8);
	}
}
