package com.mitchinmat.domain.elasticsearch.listener;

import org.apache.http.client.methods.HttpRequestWrapper;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.springframework.http.HttpStatus;

import com.mitchinmat.global.error.ErrorCode;
import com.mitchinmat.global.error.exception.NonLoggableException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ElasticSearchErrorListener implements ResponseListener {
	private final HttpRequestWrapper request;

	@Override
	public void onSuccess(Response response) {
		if (is2xxSuccessful(response)) {
			log.debug("Request [{}] succeeded with status [{}]", request.getRequestLine(), response.getStatusLine());
		} else {
			log.error("Request [{}] returned unsuccessful status [{}]", request.getRequestLine(),
				response.getStatusLine());
			throw new NonLoggableException(ErrorCode.ELASTICSEARCH_QUERY_EXECUTION_FAILED);
		}
	}

	@Override
	public void onFailure(Exception exception) {
		log.error("Request [{}] failed", request.getRequestLine(), exception);
		throw new NonLoggableException(ErrorCode.ELASTICSEARCH_CONNECTION_FAILED);
	}

	private boolean is2xxSuccessful(Response response) {
		int statusCode = response.getStatusLine().getStatusCode();
		return HttpStatus.valueOf(statusCode).is2xxSuccessful();
	}

}
