package com.mitchinmat.domain.place.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mitchinmat.domain.comment.domain.Comment;
import com.mitchinmat.global.common.BaseTimeEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Place extends BaseTimeEntity {

	@Id
	private long id;

	private String placeName;

	private String addressName;

	private String roadAddressName;

	private String phone;

	private Double x;

	private Double y;

	@Embedded
	private PlaceCrawledData placeCrawledData;

	@Embedded
	private PlaceCategory placeCategory;

	@Embedded
	private PlaceUrl placeUrl;

	@OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();

	public void addComment(Comment comment) {
		this.comments.add(comment);
	}

	public void removeComment(Comment comment) {
		this.comments.remove(comment);
	}
}
