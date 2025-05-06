package com.mitchinmat.global.util;

import static com.mitchinmat.global.error.ErrorCode.*;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchinmat.global.error.exception.MitchinmatException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JsonUtil {
	private final ObjectMapper objectMapper;

	public String toJson(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new MitchinmatException(JSON_SERIALIZATION_ERROR, e.getMessage());
		}
	}

	public <T> T fromJson(String json, TypeReference<T> typeRef) {
		try {
			return objectMapper.readValue(json, typeRef);
		} catch (JsonProcessingException e) {
			throw new MitchinmatException(JSON_DESERIALIZATION_ERROR, e.getMessage());
		}
	}
}
