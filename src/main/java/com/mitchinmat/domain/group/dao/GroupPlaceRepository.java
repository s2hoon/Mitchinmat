package com.mitchinmat.domain.group.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mitchinmat.domain.group.domain.Group;
import com.mitchinmat.domain.group.domain.GroupPlace;
import com.mitchinmat.domain.place.domain.Place;
import com.mitchinmat.domain.user.domain.User;
import com.mitchinmat.global.error.ErrorCode;
import com.mitchinmat.global.error.exception.MitchinmatException;

@Repository
public interface GroupPlaceRepository extends JpaRepository<GroupPlace, Long> {

	Optional<GroupPlace> findByGroupIdAndPlaceId(Long groupId, Long placeId);

	default GroupPlace getByGroupIdAndPlaceIdOrThrow(Long groupId, Long placeId) {
		return findByGroupIdAndPlaceId(groupId, placeId)
			.orElseThrow(() -> new MitchinmatException(ErrorCode.GROUP_PLACE_NOT_FOUND));
	}

	List<GroupPlace> findByUserAndPlaceAndGroupIn(User user, Place place, List<Group> toDelete);
}
