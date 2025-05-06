package com.mitchinmat.domain.group.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mitchinmat.domain.group.domain.Group;
import com.mitchinmat.global.error.ErrorCode;
import com.mitchinmat.global.error.exception.MitchinmatException;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>, GroupRepositoryCustom {

	List<Group> findByOwnerUserId(Long userId);

	default Group getByIdOrThrow(Long groupId) {
		return findById(groupId)
			.orElseThrow(() -> new MitchinmatException(ErrorCode.GROUP_NOT_FOUND));
	}

}
