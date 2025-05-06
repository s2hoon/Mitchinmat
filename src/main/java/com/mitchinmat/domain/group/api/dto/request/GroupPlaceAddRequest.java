package com.mitchinmat.domain.group.api.dto.request;

import java.util.List;

public record GroupPlaceAddRequest(
	List<Long> placeIds
) {

}
