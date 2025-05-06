package com.mitchinmat.domain.groupmember.api.dto.res;

import com.mitchinmat.domain.groupmember.domain.GroupInvitationCode;

import lombok.Builder;

@Builder
public record GroupInviteResponse(
	Long groupId,
	String groupName,
	String groupDescription,
	String groupCoverImageUrl,
	Integer placeCount
) {
	public static GroupInviteResponse of(GroupInvitationCode invitationCode) {
		return GroupInviteResponse.builder()
			.groupId(invitationCode.groupId())
			.groupName(invitationCode.groupName())
			.groupDescription(invitationCode.groupDescription())
			.groupCoverImageUrl(invitationCode.groupCoverImageUrl())
			.placeCount(invitationCode.placeCount())
			.build();
	}
}
