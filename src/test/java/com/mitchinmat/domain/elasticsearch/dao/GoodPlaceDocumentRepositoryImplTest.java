package com.mitchinmat.domain.elasticsearch.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;

import com.mitchinmat.domain.elasticsearch.domain.GoodPlaceDocument;
import com.mitchinmat.domain.friend.application.FriendNetworkService;

class GoodPlaceDocumentRepositoryImplTest {

	@InjectMocks
	private GoodPlaceDocumentRepositoryImpl goodPlaceDocumentRepository;

	@Mock
	private FriendNetworkService friendNetworkService;

	private final RestHighLevelClient mockClient = PowerMockito.mock(RestHighLevelClient.class);

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void 장소아이디로_좋은장소_찾기_원시데이터() throws IOException {
		// Given
		Long placeId = 123L;

		// Mock SearchResponse and SearchHits
		SearchResponse mockResponse = PowerMockito.mock(SearchResponse.class);
		SearchHits mockHits = PowerMockito.mock(SearchHits.class);
		SearchHit mockHit = PowerMockito.mock(SearchHit.class);
		TotalHits totalHits = new TotalHits(1L, TotalHits.Relation.EQUAL_TO);

		Map<String, Object> sourceMap = new HashMap<>();
		sourceMap.put("place_id", placeId);

		when(mockHit.getSourceAsMap()).thenReturn(sourceMap);
		when(mockHits.getAt(0)).thenReturn(mockHit);
		when(mockHits.getTotalHits()).thenReturn(totalHits);
		when(mockResponse.getHits()).thenReturn(mockHits);
		when(mockClient.search(any(), eq(RequestOptions.DEFAULT))).thenReturn(mockResponse);

		// When
		SearchResponse response = goodPlaceDocumentRepository.findGoodPlaceByPlaceId(placeId); // Raw 데이터 호출

		// Then
		assertNotNull(response);
		assertEquals(1L, response.getHits().getTotalHits().value);
		assertEquals(sourceMap, response.getHits().getAt(0).getSourceAsMap());
	}

	@Test
	void 좋은장소저장또는업데이트() throws IOException {
		// Given
		GoodPlaceDocument document = new GoodPlaceDocument();
		document.setPlaceId(123L);
		document.setPlaceName("Updated Place");
		document.setAddressName("456 Street");
		document.setRoadAddressName("456 Road");
		document.setUserIds(Set.of("user1", "user3"));
		// document.setPlaceCategory(new String[] {"Cafe"});
		document.setLocation(new GoodPlaceDocument.GeoPoint(37.7749, -122.4194));

		UpdateResponse mockResponse = mock(UpdateResponse.class);
		when(mockClient.update(any(), eq(RequestOptions.DEFAULT))).thenReturn(mockResponse);

		// When
		UpdateResponse result = goodPlaceDocumentRepository.saveOrUpdateGoodPlace(document);

		// Then
		assertNotNull(result);
		verify(mockClient, times(1)).update(any(), eq(RequestOptions.DEFAULT));
	}

	@Test
	void 좋은장소검색_테스트() throws IOException {
		// Given
		Long userId = 1L;
		String query = "Great Food";
		double myLat = 37.7749;
		double myLon = -122.4194;

		// Mock 친구의 친구 ID
		Set<Long> friendSet = new HashSet<>();
		friendSet.add(1L);
		friendSet.add(2L);
		String[] friendOfFriendIds = friendSet.stream().map(String::valueOf).toArray(String[]::new);
		when(friendNetworkService.getFriendAndFriendOfFriendIdsAsString(userId)).thenReturn(friendOfFriendIds);

		// Mock Elasticsearch SearchResponse
		SearchResponse mockResponse = mock(SearchResponse.class);
		SearchHits mockHits = mock(SearchHits.class);
		SearchHit mockHit = mock(SearchHit.class);

		Map<String, Object> sourceMap = new HashMap<>();
		sourceMap.put("place_id", 123L);
		sourceMap.put("place_name", "Good Place");
		sourceMap.put("address_name", "123 Street");
		sourceMap.put("road_address_name", "123 Road");
		sourceMap.put("user_id", List.of("user1", "user2"));
		sourceMap.put("place_category", List.of("Restaurant"));
		sourceMap.put("location", Map.of("lat", 37.7749, "lon", -122.4194));

		TotalHits totalHits = new TotalHits(1L, TotalHits.Relation.EQUAL_TO); // Mock TotalHits

		// Mock 설정
		when(mockHit.getSourceAsMap()).thenReturn(sourceMap);
		when(mockHit.getSortValues()).thenReturn(new Object[] {null, 1.5});
		when(mockHits.iterator()).thenReturn(Arrays.asList(mockHit).iterator());
		when(mockHits.getTotalHits()).thenReturn(totalHits); // TotalHits 설정
		when(mockHits.getAt(0)).thenReturn(mockHit); // getAt(0) 설정
		when(mockResponse.getHits()).thenReturn(mockHits);
		when(mockClient.search(any(), eq(RequestOptions.DEFAULT))).thenReturn(mockResponse);

		// When
		SearchResponse searchResponse = goodPlaceDocumentRepository.searchGoodPlace(userId, query, myLat, myLon);

		// Then
		assertNotNull(searchResponse);
		assertNotNull(searchResponse.getHits());
		assertEquals(1, searchResponse.getHits().getTotalHits().value);

		SearchHit resultHit = searchResponse.getHits().getAt(0);
		assertNotNull(resultHit); // 추가된 검사
		assertEquals("Good Place", resultHit.getSourceAsMap().get("place_name"));
		assertEquals("123 Street", resultHit.getSourceAsMap().get("address_name"));
		assertEquals(37.7749, ((Map<?, ?>)resultHit.getSourceAsMap().get("location")).get("lat"));
		assertEquals(-122.4194, ((Map<?, ?>)resultHit.getSourceAsMap().get("location")).get("lon"));
	}

}
