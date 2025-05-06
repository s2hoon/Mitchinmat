package com.mitchinmat.domain.group.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mitchinmat.domain.group.dao.GroupRepository;
import com.mitchinmat.domain.group.dao.SubscribeGroupRepository;
import com.mitchinmat.domain.group.domain.Group;
import com.mitchinmat.domain.group.domain.SubscribeGroup;
import com.mitchinmat.domain.user.dao.UserRepository;
import com.mitchinmat.domain.user.domain.User;
import com.mitchinmat.global.error.ErrorCode;
import com.mitchinmat.global.error.exception.MitchinmatException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscribeGroupService {

	private final SubscribeGroupRepository subscribeGroupRepository;
	private final GroupRepository groupRepository;
	private final UserRepository userRepository;
	@Transactional
	public void subscribe(Long userId, Long groupId) {
		if (subscribeGroupRepository.existsByUserIdAndGroupId(userId, groupId)) {
			throw new MitchinmatException(ErrorCode.GROUP_ALREADY_SUBSCRIBED);
		}
		Group group = groupRepository.getByIdOrThrow(groupId);
		User user = userRepository.getById(userId);

		SubscribeGroup subscribeGroup = SubscribeGroup.builder()
			.group(group)
			.user(user)
			.build();

		user.addSubscribedGroup(subscribeGroup);
		group.addSubscribe(subscribeGroup);
		subscribeGroupRepository.save(subscribeGroup);
	}
	@Transactional
	public void unsubscribe(Long userId, Long groupId) {
		if (!subscribeGroupRepository.existsByUserIdAndGroupId(userId, groupId)) {
			throw new MitchinmatException(ErrorCode.SUBSCRIBE_INFO_NOT_FOUND);
		}
		subscribeGroupRepository.deleteByUserIdAndGroupId(userId, groupId);
	}

}
