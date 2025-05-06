package com.mitchinmat.domain.group.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mitchinmat.domain.group.api.dto.request.GroupInsertRequest;
import com.mitchinmat.domain.group.api.dto.request.GroupUpdateRequest;
import com.mitchinmat.domain.group.api.dto.response.GroupResponse;
import com.mitchinmat.domain.group.dao.GroupRepository;
import com.mitchinmat.domain.group.domain.Group;
import com.mitchinmat.domain.user.dao.UserRepository;
import com.mitchinmat.domain.user.domain.User;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {

	@Mock
	private GroupRepository groupRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private GroupService groupService;

	@Test
	void shouldInsertGroupSuccessfully() {
		// Given
		Long userId = 1L;
		GroupInsertRequest request = new GroupInsertRequest("Test Group", true);
		User user = User.createOAuthUserBuilder().build();

		Group group = Group.builder()
			.id(1L)
			.ownerUserId(userId)
			.name(request.groupName())
			.isPublic(request.isPublic())
			.build();

		when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
		when(groupRepository.save(any(Group.class))).thenReturn(group);

		// When
		GroupResponse response = groupService.insertGroup(userId, request);

		// Then
		assertAll(
			() -> assertNotNull(response, "Response should not be null"),
			() -> assertEquals(1L, response.id(), "Group ID should match"),
			() -> assertEquals("Test Group", response.name(), "Group name should match"),
			() -> assertTrue(response.isPublic(), "Group should be public")
		);

		verify(userRepository).findById(userId);
		verify(groupRepository).save(any(Group.class));
	}

	@Test
	void testUpdateGroup_Success() {
		// Given
		Long userId = 1L;
		long groupId = 1L;
		GroupUpdateRequest request = new GroupUpdateRequest("Updated Group", "New Description", "new_image.jpg");

		Group group = Group.builder()
			.id(groupId)
			.name("Old Group")
			.description("Old Description")
			.coverImageUrl("old_image.jpg")
			.build();

		given(groupRepository.findById(groupId)).willReturn(Optional.of(group));

		// When
		GroupResponse response = groupService.updateGroup(userId, groupId, request);

		// Then
		assertAll(
			() -> assertNotNull(response),
			() -> assertEquals("Updated Group", response.name()),
			() -> assertEquals("New Description", response.description()),
			() -> assertEquals("new_image.jpg", response.coverImageUrl())
		);

		then(groupRepository).should().findById(groupId);
	}

	@Test
	void testDeleteGroup_Success() {
		// Given
		Long userId = 1L;
		long groupId = 1L;
		Group group = Group.builder()
			.id(groupId)
			.name("Test Group")
			.build();

		given(groupRepository.findById(groupId)).willReturn(Optional.of(group));

		// When
		groupService.deleteGroup(userId, groupId);

		// Then
		then(groupRepository).should().findById(groupId);
		then(groupRepository).should().delete(group);
	}

}
