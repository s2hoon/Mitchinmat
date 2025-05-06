package com.mitchinmat.domain.friend.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mitchinmat.domain.friend.domain.QFriend;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FriendRepositoryCustomImpl implements FriendRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Long> findFriendIdsByUserId(Long userId) {
		QFriend friend = QFriend.friend;

		JPAQuery<Long> query = queryFactory.select(friend.friendId)
			.from(friend)
			.where(friend.userId.eq(userId))
			.where(friend.viewStatus.isTrue());

		return query.fetch();
	}

	@Override
	public List<Long> findFriendsOfFriendsByUserId(Long userId) {
		QFriend friend = QFriend.friend;
		QFriend fof = new QFriend("fof");

		return queryFactory.select(fof.friendId)
			.distinct()
			.from(friend)
			.where(friend.viewStatus.isTrue()
				.and(friend.userId.eq(userId)))
			.join(fof).on(fof.userId.eq(friend.friendId))
			.where(fof.viewStatus.isTrue())
			.fetch();
	}
}
