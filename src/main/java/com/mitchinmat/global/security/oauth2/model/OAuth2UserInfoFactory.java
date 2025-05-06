package com.mitchinmat.global.security.oauth2.model;

import static com.mitchinmat.global.error.ErrorCode.*;

import java.util.Map;

import com.mitchinmat.global.error.exception.MitchinmatException;
import com.mitchinmat.global.security.oauth2.model.userinfo.KakaoOAuth2UserInfo;
import com.mitchinmat.global.security.oauth2.model.userinfo.OAuth2UserInfo;

public interface OAuth2UserInfoFactory {
	static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
		OAuth2Provider oAuth2Provider = OAuth2Provider.convert(registrationId);
		switch (oAuth2Provider) {
			case KAKAO:
				return new KakaoOAuth2UserInfo(attributes);
			default:
				throw new MitchinmatException(UNSUPPORTED_PROVIDER);
		}
	}
}
