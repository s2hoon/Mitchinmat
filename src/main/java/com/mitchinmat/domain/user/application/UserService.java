package com.mitchinmat.domain.user.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mitchinmat.domain.user.api.dto.response.UserInfoResponse;
import com.mitchinmat.domain.user.dao.UserRepository;
import com.mitchinmat.domain.user.domain.User;
import com.mitchinmat.global.client.kakao.KakaoClient;
import com.mitchinmat.global.client.kakao.KakaoTokenManager;
import com.mitchinmat.global.security.token.dao.RedisTokenRepository;
import com.mitchinmat.global.util.CookieUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;
	private final RedisTokenRepository redisTokenRepository;
	private final KakaoClient kakaoClient;
	private final KakaoTokenManager kakaoTokenManager;

	@Value("${auth.token.refresh-cookie-key}")
	private String cookieKey;

	public UserInfoResponse findUserInfo(Long userId) {
		return UserInfoResponse.of(userRepository.getById(userId));
	}

	public boolean checkKakaoFriendsAgreementScope(Long userId) {
		User user = userRepository.getById(userId);
		String kakaoAccessToken = kakaoTokenManager.getOrReissueAccessToken(user.getId());
		return kakaoClient.friendsListAgreed(kakaoAccessToken);
	}

	@Transactional
	public void removeUser(HttpServletRequest request, HttpServletResponse response, Long userId) {
		User user = userRepository.getById(userId);
		String kakaoAccessToken = kakaoTokenManager.getOrReissueAccessToken(user.getId());

		kakaoClient.unlinkUser(kakaoAccessToken);
		user.changeUserStatusToInActive();

		CookieUtils.deleteCookie(request, response, cookieKey);
		redisTokenRepository.deleteAllTokens(user.getId());
	}

}
