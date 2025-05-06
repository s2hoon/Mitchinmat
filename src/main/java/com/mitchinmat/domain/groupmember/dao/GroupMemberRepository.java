package com.mitchinmat.domain.groupmember.dao;

import static com.mitchinmat.global.error.ErrorCode.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.mitchinmat.domain.groupmember.domain.GroupMember;
import com.mitchinmat.global.error.exception.MitchinmatException;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

	boolean existsByGroupIdAndUserId(@Param("groupId") Long groupId, @Param("userId") Long userId);

	Optional<GroupMember> findByGroupIdAndUserId(Long groupId, Long userId);

	List<GroupMember> findByGroupId(Long groupId);

	default void checkMemberExistsInGroup(Long groupId, Long userId) {
		if (!existsByGroupIdAndUserId(groupId, userId)) {
			throw new MitchinmatException(GROUP_PERMISSION_REFUSED);
		}
	}

	default GroupMember getByGroupIdAndUserId(Long groupId, Long userId) {
		return findByGroupIdAndUserId(groupId, userId)
			.orElseThrow(() -> new MitchinmatException(GROUP_MEMBER_NOT_FOUND));
	}
}
