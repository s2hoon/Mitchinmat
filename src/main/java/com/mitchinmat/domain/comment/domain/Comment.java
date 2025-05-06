package com.mitchinmat.domain.comment.domain;

import com.mitchinmat.domain.place.domain.Place;
import com.mitchinmat.domain.user.domain.User;
import com.mitchinmat.global.common.BaseTimeEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	private Place place;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	public void updateContent(String newContent) {
		this.content = newContent;
	}

}