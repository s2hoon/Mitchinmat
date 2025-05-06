package com.mitchinmat.domain.elasticsearch.application;

import static com.mitchinmat.domain.elasticsearch.domain.GoodPlaceDocumentFactory.*;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mitchinmat.domain.elasticsearch.domain.GoodPlaceDocument;
import com.mitchinmat.domain.goodplace.dao.GoodPlaceRepository;
import com.mitchinmat.domain.goodplace.domain.GoodPlace;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElasticDataConsistencyService {

	private final GoodPlaceRepository goodPlaceRepository;
	private final GoodPlaceElasticSearchService goodPlaceElasticSearchService;

	public void checkAndFixDataConsistency() {
		List<GoodPlace> goodPlaces = goodPlaceRepository.findAllGoodPlacesWithDetails();
		int updateCount = 0;

		for (GoodPlace goodPlace : goodPlaces) {
			GoodPlaceDocument document = goodPlaceElasticSearchService.findGoodPlaceByPlaceId(
				goodPlace.getPlace().getId());

			if (document == null) {
				saveNewDocument(goodPlace);
			} else if (needsUpdate(document, goodPlace)) {
				updateDocument(document, goodPlace);
				updateCount++;
			}
		}

		log.info("Updated documents count: {}", updateCount);
	}

	private void saveNewDocument(GoodPlace goodPlace) {
		Long userId = goodPlace.getUser().getId();
		GoodPlaceDocument newDocument = createFromGoodPlace(goodPlace, userId);
		goodPlaceElasticSearchService.saveOrUpdateGoodPlace(newDocument);
		goodPlaceElasticSearchService.refreshIndex("goodplaces");
	}

	private boolean needsUpdate(GoodPlaceDocument document, GoodPlace goodPlace) {
		String newUserId = goodPlace.getUser().getId().toString();
		return !document.getUserIds().contains(newUserId);
	}

	private void updateDocument(GoodPlaceDocument document, GoodPlace goodPlace) {
		Long userId = goodPlace.getUser().getId();
		GoodPlaceDocument updatedDocument = updateWithUserId(document, userId);
		goodPlaceElasticSearchService.saveOrUpdateGoodPlace(updatedDocument);
		goodPlaceElasticSearchService.refreshIndex("goodplaces");
	}
}



