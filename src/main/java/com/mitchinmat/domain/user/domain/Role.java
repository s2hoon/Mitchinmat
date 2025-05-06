package com.mitchinmat.domain.user.domain;

import lombok.Getter;

@Getter
public enum Role {

	USER("ROLE_USER"),
	ADMIN("ROLE_ADMIN"),
	ANONYMOUS("ROLE_ANONYMOUS");

	private final String authority;

	Role(String authority) {
		this.authority = authority;
	}
}