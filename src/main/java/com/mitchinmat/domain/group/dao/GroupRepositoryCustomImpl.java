package com.mitchinmat.domain.group.dao;

import static com.mitchinmat.domain.group.domain.QGroupPlace.*;
import static com.mitchinmat.domain.groupmember.domain.QGroupMember.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mitchinmat.domain.group.domain.Group;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GroupRepositoryCustomImpl implements GroupRepositoryCustom{

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Group> findGroupIdsByUserAndPlace(Long userId, Long placeId) {

		List<Long> groupIds = queryFactory
			.select(groupMember.group.id)
			.from(groupMember)
			.where(groupMember.user.id.eq(userId))
			.fetch();

		return queryFactory
			.select(groupPlace.group)
			.from(groupPlace)
			.where(
				groupPlace.group.id.in(groupIds),
				groupPlace.place.id.eq(placeId)
			)
			.fetch();
	}
}
