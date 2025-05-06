package com.mitchinmat.global.security.oauth2.model.userinfo;

import java.util.Map;
import java.util.Objects;

import com.mitchinmat.global.security.oauth2.model.OAuth2Provider;

import lombok.Getter;

@Getter
public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

	private final Map<String, Object> attributes;

	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "nickname";
	private static final String KEY_PROFILE = "profile";
	private static final String KEY_IMAGE_URL = "profile_image_url";
	private static final String KEY_KAKAO_ACCOUNT = "kakao_account";

	public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
		super(OAuth2Provider.KAKAO);
		this.attributes = attributes;
	}

	@Override
	public String getOauth2Id() {
		return Objects.toString(attributes.get(KEY_ID));
	}

	@Override
	public String getUsername() {
		return (String)this.getProfile().get(KEY_NAME);
	}

	@Override
	public String getProfileImage() {
		return (String)this.getProfile().get(KEY_IMAGE_URL);
	}

	@SuppressWarnings({"unchecked"})
	@Override
	public Map<String, Object> getExtraData() {
		return attributes;
	}

	private Map<String, Object> getKakaoAccount() {
		return (Map<String, Object>)attributes.get(KEY_KAKAO_ACCOUNT);
	}

	@SuppressWarnings({"unchecked"})
	private Map<String, Object> getProfile() {
		return (Map<String, Object>)this.getKakaoAccount().get(KEY_PROFILE);
	}
}
