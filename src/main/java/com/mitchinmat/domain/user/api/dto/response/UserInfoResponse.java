package com.mitchinmat.domain.user.api.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mitchinmat.domain.user.domain.User;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserInfoResponse(
	Long userId,
	String username,
	String profileImage,
	Long goodPlaceCount,
	Long groupCount
) {
	public static UserInfoResponse of(User user) {
		return new UserInfoResponse(
			user.getId(),
			user.getUsername(),
			user.getProfileImage(),
			user.getGoodPlaceCount(),
			user.getGroupCount()
		);
	}

}
