package com.mitchinmat.domain.user.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.mitchinmat.domain.comment.domain.Comment;
import com.mitchinmat.domain.friend.domain.Friend;
import com.mitchinmat.domain.friend.domain.FriendOfFriend;
import com.mitchinmat.domain.group.domain.SubscribeGroup;
import com.mitchinmat.global.common.BaseTimeEntity;
import com.mitchinmat.global.security.oauth2.model.OAuth2Provider;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
public class User extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String oauth2Id;

	@NotNull
	@Enumerated(EnumType.STRING)
	private OAuth2Provider provider;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Role role;

	private String username;

	private String profileImage;

	private Long goodPlaceCount;

	private Long groupCount;

	@NotNull
	@Enumerated(EnumType.STRING)
	private UserStatus status;
	private LocalDateTime inactiveDate;

	private LocalDateTime friendSyncedDate;

	private boolean goodPlacesPublicStatus;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<Friend> friends = new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private FriendOfFriend friendOfFriend;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SubscribeGroup> subscribedGroups = new ArrayList<>();

	@Builder(builderMethodName = "createOAuthUserBuilder")
	public User(String oauth2Id, OAuth2Provider provider, Role role, UserStatus status, String username,
		String profileImage) {
		this.username = username;
		this.oauth2Id = oauth2Id;
		this.provider = provider;
		this.role = role;
		this.profileImage = profileImage;
		this.goodPlaceCount = 0L;
		this.groupCount = 0L;
		this.status = status;
	}

	public void updateInfo(String username, String profileImage) {
		this.username = username;
		this.profileImage = profileImage;
	}

	public void updateFriendSyncedDate() {
		friendSyncedDate = LocalDateTime.now();
	}

	public void syncFriendOfFriend(FriendOfFriend friendOfFriend) {
		this.friendOfFriend = friendOfFriend;
	}

	public void changeUserStatusToActive() {
		this.status = UserStatus.ACTIVE;
	}

	public void changeUserStatusToInActive() {
		this.inactiveDate = LocalDateTime.now();
		this.status = UserStatus.INACTIVE;
	}

	public void plusGoodPlaceCount() {
		this.goodPlaceCount = this.goodPlaceCount + 1;
	}

	public void minusGoodPlaceCount() {
		this.goodPlaceCount = this.goodPlaceCount - 1;
	}

	public void plusGroupCount() {
		this.groupCount = this.groupCount + 1;
	}

	public void minusGroupCount() {
		this.groupCount = this.groupCount - 1;
	}

	public void addComment(Comment comment) {
		this.comments.add(comment);
	}

	public void removeComment(Comment comment) {
		this.comments.remove(comment);
	}

	public void addSubscribedGroup(SubscribeGroup subscribeGroup) {
		this.subscribedGroups.add(subscribeGroup);
	}

	public void removeSubscribedGroup(SubscribeGroup subscribeGroup) {
		this.subscribedGroups.remove(subscribeGroup);
	}

	public void toggleGoodPlacesPublicStatus() {
		this.goodPlacesPublicStatus = !this.goodPlacesPublicStatus;
	}
}
