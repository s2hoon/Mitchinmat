package com.mitchinmat.global.logback.discord;

import static com.mitchinmat.global.error.ErrorCode.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.mitchinmat.global.error.exception.MitchinmatException;
import com.mitchinmat.global.util.RestTemplateUtils;

public class DiscordWebHook {

	private final String urlString;

	public DiscordWebHook(String urlString) {
		this.urlString = urlString;
	}

	public void send(String payload) {
		HttpHeaders headers = RestTemplateUtils.createHeaders(MediaType.APPLICATION_JSON);
		ResponseEntity<String> response = RestTemplateUtils.sendPostRequest(urlString, headers, payload, String.class);

		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new MitchinmatException(EXTERNAL_API_BAD_REQUEST, "Failed to execute Discord webhook");
		}
	}
}
