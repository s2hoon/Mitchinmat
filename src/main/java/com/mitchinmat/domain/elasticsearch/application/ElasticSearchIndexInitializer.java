package com.mitchinmat.domain.elasticsearch.application;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.mitchinmat.global.error.ErrorCode;
import com.mitchinmat.global.error.exception.MitchinmatException;

import io.micrometer.core.instrument.util.IOUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ElasticSearchIndexInitializer {

	private final RestHighLevelClient client;

	@PostConstruct
	public void initializeIndex() {
		String indexName = "goodplaces";
		try {
			if (!indexExists(indexName)) {
				createIndex(indexName);
			}
		} catch (Exception e) {
			throw new MitchinmatException(ErrorCode.ELASTICSEARCH_CONNECTION_FAILED);
		}
	}

	private boolean indexExists(String indexName) throws IOException {
		GetIndexRequest request = new GetIndexRequest(indexName);
		return client.indices().exists(request, RequestOptions.DEFAULT);
	}

	private void createIndex(String indexName) throws Exception {
		CreateIndexRequest request = new CreateIndexRequest(indexName);
		String indexSettingsAndMappings = readIndexSettings();
		request.source(indexSettingsAndMappings, XContentType.JSON);
		CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
		if (response.isAcknowledged()) {
			log.info("Index created successfully: " + indexName);
		} else {
			log.info("Index creation failed: " + indexName);
		}
	}

	private String readIndexSettings() throws IOException {
		ClassPathResource resource = new ClassPathResource("db/es-index.json");
		return IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
	}
}
