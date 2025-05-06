package com.mitchinmat.global.security.oauth2.model;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomOAuth2User extends DefaultOAuth2User {

	private static final String USER_ID = "userId";
	private static final String OAUTH2_ID = "oauth2Id";
	private static final String SUB = "sub";

	private Long userId;
	private String oauth2Id;

	@Builder
	public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
		Map<String, Object> attributes,
		String nameAttributeKey,
		Long userId,
		String oauth2Id) {
		super(authorities, attributes, nameAttributeKey);
		this.userId = userId;
		this.oauth2Id = oauth2Id;
	}

	public static CustomOAuth2User of(Long userId, String oauth2Id,
		Collection<? extends GrantedAuthority> authorities) {

		Map<String, Object> attributes = Map.of(USER_ID, userId, OAUTH2_ID, oauth2Id, SUB, oauth2Id);
		String nameAttributeKey = SUB;

		return CustomOAuth2User.builder()
			.userId(userId)
			.oauth2Id(oauth2Id)
			.authorities(authorities)
			.attributes(attributes)
			.nameAttributeKey(nameAttributeKey)
			.build();
	}
}
