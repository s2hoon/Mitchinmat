package com.mitchinmat.global.error.exception;

import com.mitchinmat.global.error.ErrorCode;

import lombok.Getter;

@Getter
public class NonLoggableException extends RuntimeException {

	private final ErrorCode errorCode;

	public NonLoggableException(ErrorCode errorCode, String message) {
		super(errorCode.getMessage() + ": " + message);
		this.errorCode = errorCode;
	}

	public NonLoggableException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public NonLoggableException(ErrorCode errorCode, String additionalMessage, String provider) {
		super(errorCode.getMessage() + " " + provider + additionalMessage);
		this.errorCode = errorCode;
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

}
