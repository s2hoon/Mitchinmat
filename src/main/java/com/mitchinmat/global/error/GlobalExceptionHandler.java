package com.mitchinmat.global.error;

import static com.mitchinmat.global.error.ErrorCode.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mitchinmat.global.common.response.ApiResponse;
import com.mitchinmat.global.common.response.ResponseUtils;
import com.mitchinmat.global.error.exception.MitchinmatException;
import com.mitchinmat.global.error.exception.NonLoggableException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * 객체, 파라미터 데이터 값이 유효하지 않은 경우
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ApiResponse<Map<String, Object>> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException e) {
		log.error("MethodArgumentNotValidException {}", e.getMessage(), e);
		Map<String, String> errors = new HashMap<>();

		BindingResult bindingResult = e.getBindingResult();
		for (FieldError er : bindingResult.getFieldErrors()) {
			if (errors.containsKey(er.getField())) {
				errors.put(er.getField(), errors.get(er.getField()) + " " + er.getDefaultMessage());
			} else {
				errors.put(er.getField(), er.getDefaultMessage());
			}
		}

		Map<String, Object> data = new HashMap<>();
		data.put("errors", errors);
		return ResponseUtils.error(INVALID_TYPE_VALUE.getCode(), data, INVALID_TYPE_VALUE.getMessage());
	}

	/**
	 * 커스텀 예외
	 */
	@ExceptionHandler(value = MitchinmatException.class)
	public ResponseEntity<ApiResponse<Void>> handleMitchinmatException(MitchinmatException e) {
		ErrorCode errorCode = e.getErrorCode();
		log.error("MitchinmatException {}", errorCode.getMessage(), e);
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(ResponseUtils.error(errorCode.getCode(), errorCode.getMessage()));
	}

	@ExceptionHandler(value = NonLoggableException.class)
	public ResponseEntity<ApiResponse<Void>> handleNonLoggableException(NonLoggableException e) {
		ErrorCode errorCode = e.getErrorCode();
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(ResponseUtils.error(errorCode.getCode(), errorCode.getMessage()));
	}

	@ExceptionHandler(value = RuntimeException.class)
	public ResponseEntity<ApiResponse<Void>> handleRunttimeEx(RuntimeException e) {
		log.error("RuntimeException {}", e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ResponseUtils.error(UNDEFINED_ERROR.getCode(), "정의되지 않은 에러 : " + e.getMessage()));
	}

}
