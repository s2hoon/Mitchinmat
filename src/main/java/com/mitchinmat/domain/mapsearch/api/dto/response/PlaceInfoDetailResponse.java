package com.mitchinmat.domain.mapsearch.api.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.mitchinmat.domain.comment.domain.Comment;
import com.mitchinmat.domain.group.api.dto.response.GroupResponse;
import com.mitchinmat.domain.group.domain.Group;
import com.mitchinmat.domain.place.domain.Place;
import com.mitchinmat.domain.place.domain.PlaceCategory;
import com.mitchinmat.domain.place.domain.PlaceCrawledData;
import com.mitchinmat.domain.place.domain.PlaceUrl;
import com.mitchinmat.domain.user.api.dto.response.UserInfoResponse;
import com.mitchinmat.domain.user.domain.User;

public record PlaceInfoDetailResponse(
	Long placeId,
	String placeName,
	String addressName,
	String roadAddressName,
	String phoneNumber,
	Double x,
	Double y,
	PlaceCrawledData placeCrawledData,
	PlaceCategory placeCategory,
	PlaceUrl placeUrl,
	boolean isGoodPlace,
	boolean isWishPlace,
	List<UserInfoResponse> friendList,
	List<GroupResponse> groupList,
	List<Comment> comments
) {
	public static PlaceInfoDetailResponse of(Place place, boolean isGoodPlace, boolean isWishPlace, List<User> friendList, List<Group> groupList) {
		return new PlaceInfoDetailResponse(
			place.getId(),
			place.getPlaceName(),
			place.getAddressName(),
			place.getRoadAddressName(),
			place.getPhone(),
			place.getX(),
			place.getY(),
			place.getPlaceCrawledData(),
			place.getPlaceCategory(),
			place.getPlaceUrl(),
			isGoodPlace,
			isWishPlace,
			friendList.stream().map(UserInfoResponse::of).collect(Collectors.toList()),
			groupList.stream().map(GroupResponse::from).collect(Collectors.toList()),
			place.getComments()
		);
	}
}
