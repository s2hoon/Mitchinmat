package com.mitchinmat.domain.user.application;

import static com.mitchinmat.global.error.ErrorCode.*;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mitchinmat.domain.friend.dao.FriendRepository;
import com.mitchinmat.domain.friend.domain.Friend;
import com.mitchinmat.domain.user.dao.UserRepository;
import com.mitchinmat.domain.user.domain.Role;
import com.mitchinmat.domain.user.domain.User;
import com.mitchinmat.domain.user.domain.UserStatus;
import com.mitchinmat.global.error.exception.MitchinmatException;
import com.mitchinmat.global.security.oauth2.model.OAuth2Provider;
import com.mitchinmat.global.security.oauth2.model.userinfo.OAuth2UserInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

	private final UserRepository userRepository;
	private final FriendRepository friendRepository;

	@Transactional
	public User registerUser(OAuth2UserInfo oAuth2UserInfo) {
		Optional<User> existingUser = userRepository.findByOauth2Id(oAuth2UserInfo.getOauth2Id());
		return existingUser.map(user -> updateUser(user, oAuth2UserInfo))
			.orElseGet(() -> createNewUser(oAuth2UserInfo));
	}

	private User updateUser(User user, OAuth2UserInfo oAuth2UserInfo) {
		validateSameProvider(user, oAuth2UserInfo.getProvider());
		user.updateInfo(oAuth2UserInfo.getUsername(), convertToHttps(oAuth2UserInfo.getProfileImage()));
		if (user.getStatus() == UserStatus.INACTIVE) {
			user.changeUserStatusToActive();
		}
		return user;
	}

	private User createNewUser(OAuth2UserInfo oAuth2UserInfo) {
		User newUser = userRepository.save(
			User.createOAuthUserBuilder()
				.provider(oAuth2UserInfo.getProvider())
				.oauth2Id(oAuth2UserInfo.getOauth2Id())
				.username(oAuth2UserInfo.getUsername())
				.profileImage(convertToHttps(oAuth2UserInfo.getProfileImage()))
				.role(Role.USER)
				.status(UserStatus.ACTIVE)
				.build()
		);
		setDummyFriend(newUser.getId());
		return newUser;
	}

	private void setDummyFriend(long userId) {
		Friend dummy = Friend.builder().userId(userId).friendId(999L).viewStatus(true)
			.build();
		friendRepository.save(dummy);
	}

	private void validateSameProvider(User user, OAuth2Provider provider) {
		if (!user.getProvider().equals(provider)) {
			throw new MitchinmatException(ALREADY_SIGN_UP_OTHER_PROVIDER, user.getProvider().name(), "로 로그인하세요.");
		}
	}

	private String convertToHttps(String url) {
		return url.startsWith("http://") ? url.replace("http://", "https://") : url;
	}
}
