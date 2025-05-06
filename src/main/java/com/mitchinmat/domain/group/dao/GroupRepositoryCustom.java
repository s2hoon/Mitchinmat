package com.mitchinmat.domain.group.dao;

import java.util.List;

import com.mitchinmat.domain.group.domain.Group;

public interface GroupRepositoryCustom {

	List<Group> findGroupIdsByUserAndPlace(Long userId, Long placeId);
}
