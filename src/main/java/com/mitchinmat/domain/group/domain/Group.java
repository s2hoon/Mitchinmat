package com.mitchinmat.domain.group.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mitchinmat.domain.groupmember.domain.GroupMember;
import com.mitchinmat.global.common.BaseTimeEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "group_info")
public class Group extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long ownerUserId;

	private String name;

	private String description;

	private String coverImageUrl;

	private int placeCount;

	private boolean isPublic;

	private boolean isCollaborative;

	private String viewCode;

	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<GroupMember> members = new ArrayList<>();

	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<GroupPlace> places = new ArrayList<>();

	@OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SubscribeGroup> subscribeGroups = new ArrayList<>();

	public void updateGroupInfo(String name, String description, String coverImageUrl) {
		this.name = name;
		this.description = description;
		this.coverImageUrl = coverImageUrl;
	}

	public Boolean togglePublicStatus() {
		this.isPublic = !this.isPublic;
		return this.isPublic;
	}

	public void setViewCode() {
		viewCode = String.format("%d-%s", System.currentTimeMillis(), UUID.randomUUID().toString().substring(0, 4));
	}

	public void addMember(GroupMember groupMember) {
		this.members.add(groupMember);
	}
	
	public void addGroupPlace(GroupPlace groupPlace) {
		this.places.add(groupPlace);
	}

	public void addSubscribe(SubscribeGroup subscribeGroup) {
		this.subscribeGroups.add(subscribeGroup);
	}

}
