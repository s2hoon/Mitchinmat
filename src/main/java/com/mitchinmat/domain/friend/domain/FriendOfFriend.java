package com.mitchinmat.domain.friend.domain;

import com.mitchinmat.global.common.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FriendOfFriend extends BaseTimeEntity {
	@Id
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "friend_id_list")
	private String friendIds;

	public void updateFriendIds(String friendIds) {
		this.friendIds = friendIds;
	}

}
