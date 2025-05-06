package com.mitchinmat.domain.group.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mitchinmat.domain.group.domain.SubscribeGroup;

@Repository
public interface SubscribeGroupRepository extends JpaRepository<SubscribeGroup,Long> {

	boolean existsByUserIdAndGroupId(Long userId, Long groupId);

	void deleteByUserIdAndGroupId(Long userId, Long groupId);

	List<SubscribeGroup> findByUserId(Long userId);
}
