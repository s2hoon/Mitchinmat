package com.mitchinmat.domain.groupmember.application;

import static com.mitchinmat.global.error.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mitchinmat.domain.group.dao.GroupRepository;
import com.mitchinmat.domain.group.domain.Group;
import com.mitchinmat.domain.groupmember.api.dto.res.GroupInviteResponse;
import com.mitchinmat.domain.groupmember.api.dto.res.GroupMemberResponse;
import com.mitchinmat.domain.groupmember.dao.GroupInvitationRepository;
import com.mitchinmat.domain.groupmember.dao.GroupMemberRepository;
import com.mitchinmat.domain.groupmember.domain.GroupInvitationCode;
import com.mitchinmat.domain.groupmember.domain.GroupMember;
import com.mitchinmat.domain.user.dao.UserRepository;
import com.mitchinmat.domain.user.domain.User;
import com.mitchinmat.global.error.exception.MitchinmatException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupMemberService {

	private final UserRepository userRepository;
	private final GroupRepository groupRepository;
	private final GroupMemberRepository groupMemberRepository;
	private final GroupInvitationRepository groupInvitationRepository;

	@Transactional
	public String issueInviteCode(Long userId, Long groupId) {
		User user = userRepository.getById(userId);
		Group group = groupRepository.getByIdOrThrow(groupId);

		// groupMemberRepository.checkMemberExistsInGroup(groupId, userId);

		Optional<String> existingCodeOpt = groupInvitationRepository.findCodeByGroupId(groupId);
		if (existingCodeOpt.isPresent()) {
			return existingCodeOpt.get();
		}

		GroupInvitationCode invitationCode = GroupInvitationCode.create(group);
		groupInvitationRepository.save(invitationCode);

		return invitationCode.code();
	}

	public GroupInviteResponse getGroupInfoByInviteCode(String code) {
		Optional<GroupInvitationCode> invitationOpt = groupInvitationRepository.findGroupInvitationInfoByCode(code);
		return invitationOpt.map(GroupInviteResponse::of)
			.orElseThrow(() -> new MitchinmatException(GROUP_INVITATION_NOT_FOUND));
	}

	@Transactional
	public void acceptInvite(Long userId, String code) {
		User user = userRepository.getById(userId);
		GroupInvitationCode invitationCode = groupInvitationRepository.findGroupInvitationInfoByCode(code)
			.orElseThrow(() -> new MitchinmatException(GROUP_INVITATION_NOT_FOUND));

		if (invitationCode.expirationTime().isBefore(LocalDateTime.now())) {
			groupInvitationRepository.deleteByCode(code);
			groupInvitationRepository.deleteGroupMapping(invitationCode.groupId());
			throw new MitchinmatException(GROUP_INVITATION_NOT_FOUND);
		}

		Group group = groupRepository.getByIdOrThrow(invitationCode.groupId());
		if (!groupMemberRepository.existsByGroupIdAndUserId(group.getId(), user.getId())) {
			groupMemberRepository.save(GroupMember.builder()
				.group(group)
				.user(user)
				.build());
		}
	}

	public List<GroupMemberResponse> getGroupMembers(Long groupId) {
		Group group = groupRepository.getByIdOrThrow(groupId);
		List<GroupMember> members = groupMemberRepository.findByGroupId(groupId);
		return members.stream()
			.map(GroupMemberResponse::of)
			.toList();
	}

	@Transactional
	public void quitGroup(Long userId, Long groupId) {
		GroupMember groupMember = groupMemberRepository.getByGroupIdAndUserId(groupId, userId);
		groupMemberRepository.delete(groupMember);
	}
}
