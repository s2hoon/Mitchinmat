package com.mitchinmat.domain.groupmember.dao;

import static com.mitchinmat.global.error.ErrorCode.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mitchinmat.domain.groupmember.domain.GroupInvitationCode;
import com.mitchinmat.global.error.exception.MitchinmatException;
import com.mitchinmat.global.util.JsonUtil;
import com.mitchinmat.global.util.RedisUtil;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GroupInvitationRepository {
	private final RedisUtil redisUtil;
	private final JsonUtil jsonUtil;

	private String generateGroupMappingKey(Long groupId) {
		return String.format("GROUP_INVITATION:GROUP:%d", groupId);
	}

	private String generateCodeKey(String code) {
		return String.format("GROUP_INVITATION:CODE:%s", code);
	}

	public void save(GroupInvitationCode info) {
		String codeKey = generateCodeKey(info.code());
		long expiresInMillis = Duration.between(LocalDateTime.now(), info.expirationTime()).toMillis();
		String json = jsonUtil.toJson(info);
		try {
			redisUtil.save(generateGroupMappingKey(info.groupId()), info.code(), expiresInMillis);
			redisUtil.save(codeKey, json, expiresInMillis);
		} catch (Exception e) {
			throw new MitchinmatException(GROUP_INVITATION_SAVE_FAILED);
		}
	}

	public Optional<GroupInvitationCode> findGroupInvitationInfoByCode(String code) {
		String codeKey = generateCodeKey(code);
		Optional<String> infoOptional = redisUtil.find(codeKey);
		if (infoOptional.isPresent()) {
			GroupInvitationCode groupInvitationCode = jsonUtil.fromJson(infoOptional.get(),
				new TypeReference<GroupInvitationCode>() {
				});
			return Optional.of(groupInvitationCode);
		}
		return Optional.empty();
	}

	public Optional<String> findCodeByGroupId(Long groupId) {
		String groupMappingKey = generateGroupMappingKey(groupId);
		Optional<String> codeOptional = redisUtil.find(groupMappingKey);
		if (codeOptional.isPresent()) {
			Optional<GroupInvitationCode> infoOpt = findGroupInvitationInfoByCode(codeOptional.get());
			if (!infoOpt.isPresent()) {
				deleteGroupMapping(groupId);
				return Optional.empty();
			}
		}
		return codeOptional;
	}

	public void deleteByCode(String code) {
		String codeKey = generateCodeKey(code);
		redisUtil.delete(codeKey);
	}

	public void deleteGroupMapping(Long groupId) {
		String groupMappingKey = generateGroupMappingKey(groupId);
		redisUtil.delete(groupMappingKey);
	}
}
