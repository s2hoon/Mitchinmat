package com.mitchinmat.domain.groupmember.api.dto.res;

import com.mitchinmat.domain.groupmember.domain.GroupMember;
import com.mitchinmat.domain.user.domain.User;

public record GroupMemberResponse(
	Long userId,
	String username,
	String profileImage
) {
	public static GroupMemberResponse of(GroupMember groupMember) {
		User user = groupMember.getUser();
		return new GroupMemberResponse(user.getId(), user.getUsername(), user.getProfileImage());
	}
}
