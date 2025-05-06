package com.mitchinmat.global.security.oauth2.model.userinfo;

import java.util.Map;

import com.mitchinmat.global.security.oauth2.model.OAuth2Provider;

public abstract class OAuth2UserInfo {
	protected OAuth2Provider provider;

	public OAuth2UserInfo(OAuth2Provider provider) {
		this.provider = provider;
	}

	public OAuth2Provider getProvider() {
		return provider;
	}

	public abstract String getOauth2Id();

	public abstract String getUsername();

	public abstract String getProfileImage();

	public abstract Map<String, Object> getExtraData();

	@Override
	public String toString() {
		return "OAuth2UserInfo{" +
			"oauth2Id=" + getOauth2Id() +
			", provider=" + provider +
			", username=" + getUsername() +
			", profileImage=" + getProfileImage() +
			'}';
	}
}
