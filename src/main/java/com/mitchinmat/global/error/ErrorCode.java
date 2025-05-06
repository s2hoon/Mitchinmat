package com.mitchinmat.global.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	/**
	 * USER(A000)
	 */
	USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "A001", "존재하지 않는 유저입니다"),
	LOGIN_FAILED(HttpStatus.BAD_REQUEST, "A002", "로그인 실패"),
	AUTH_FAILED(HttpStatus.BAD_REQUEST, "A003", "인증 실패"),
	USER_UNLINK_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "A004", "유저 탈퇴 실패"),
	ACCESS_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "A005", "Access 토큰 찾을 수 없음"),

	/**
	 * FRIEND(B000)
	 */
	FRIEND_NOT_EXIST(HttpStatus.BAD_REQUEST, "B001", "존재하지 않는 친구입니다"),
	SYNC_NOT_DONE(HttpStatus.BAD_REQUEST, "B002", "친구 동기화를 먼저 진행해주세요"),

	/**
	 * PLACE(C000)
	 */
	PLACE_NOT_EXIST(HttpStatus.BAD_REQUEST, "C001", "존재하지 않는 음식점입니다."),
	PLACE_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "C002", "이미 존재하는 음식점 ID"),
	PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "C003", "음식점을 찾을 수 없음"),
	PLACE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "C004", "음식점 삭제 실패"),
	PLACE_INSERT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "C005", "음식점 추가 실패"),

	/**
	 * WISH_PLACE(D000)
	 */
	WISH_PLACE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "D001", "이미 존재하는 가고 싶은 장소입니다."),
	WISH_PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "D002", "위시리스트에서 찾을 수 없습니다."),
	WISH_PLACE_FORBIDDEN(HttpStatus.FORBIDDEN, "D003", "해당 위시리스트를 삭제할 권한이 없습니다."),

	/**
	 * GOOD_PLACE(E000)
	 */
	GOOD_PLACE_NOT_EXIST(HttpStatus.NOT_FOUND, "E001", "나의 맛집으로 등록된 음식점이 없습니다."),
	GOOD_PLACE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "E002", "나의 맛집으로 이미 등록되어 있습니다."),
	GOOD_PLACE_FAILED_GENERATE_REDIS_KEY(HttpStatus.BAD_REQUEST, "E003", "Redis 키 생성 실패"),

	/**
	 * KAKAO API(F000)
	 */
	KAKAO_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "F001", "카카오 서버 에러"),
	KAKAO_ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "F002", "카카오 Access 토큰이 만료되었습니다. 재로그인해주세요"),
	KAKAO_REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "F002", "카카오 Refresh 토큰이 만료되었습니다. 재로그인해주세요"),
	KAKAO_RESPONSE_PARSING_FAILED(HttpStatus.BAD_REQUEST, "F003", "친구 리스트 파싱에 실패했습니다."),
	KAKAO_SYNC_FAILED(HttpStatus.BAD_REQUEST, "F004", "카카오 친구 동기화에 실패했습니다. 권한 확인 필요."),
	KAKAO_BAD_REQUEST(HttpStatus.BAD_REQUEST, "F005", "카카오 API 400번대 에러"),
	KAKAO_ACCESS_TOKEN_RETRIEVAL_FAILED(HttpStatus.BAD_REQUEST, "F006", "카카오 액세스 토큰 획득 실패"),
	KAKAO_USER_INFO_RETRIEVAL_FAILED(HttpStatus.BAD_REQUEST, "F007", "카카오 사용자 정보 획득 실패"),

	/**
	 * Auth(G000)
	 */
	ALREADY_SIGN_UP_OTHER_PROVIDER(HttpStatus.BAD_REQUEST, "G001", "다른 소셜 계정으로 가입되어있습니다."),
	UNSUPPORTED_PROVIDER(HttpStatus.BAD_REQUEST, "지원하지 않는 소셜입니다.", ""),

	/**
	 * JWT Token(H000)
	 */
	TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "H001", "유효하지 않은 토큰입니다."),
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "H002", "만료된 토큰입니다."),
	TOKEN_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "H003", "지원되지 않는 토큰입니다."),
	TOKEN_WRONG(HttpStatus.UNAUTHORIZED, "H004", "잘못된 토큰 서명입니다."),
	REFRESH_TOKEN_NOT_EXIST_IN_COOKIE(HttpStatus.UNAUTHORIZED, "H005", "쿠키에 Refresh 토큰이 없습니다."),
	REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "H006", "만료된 Refresh 토큰 입니다. 재로그인해주세요"),
	TOKEN_NOT_MATCHED(HttpStatus.UNAUTHORIZED, "H007", "토큰이 일치하지 않습니다."),

	/**
	 * 외부 API(I000)
	 */
	EXTERNAL_API_NOT_FOUND(HttpStatus.NOT_FOUND, "I001", "외부 API를 찾을 수 없습니다."),
	EXTERNAL_API_METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "I002", "허용되지 않은 HTTP 메서드입니다."),
	EXTERNAL_API_UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "I003", "지원되지 않는 미디어 타입입니다."),
	EXTERNAL_API_BAD_REQUEST(HttpStatus.INTERNAL_SERVER_ERROR, "I004", "잘못된 요청입니다."),
	EXTERNAL_API_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "I005", "서버 내부 오류가 발생했습니다."),

	/**
	 * Group(J000)
	 */
	GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "J001", "그룹을 찾을 수 없습니다."),
	GROUP_PERMISSION_REFUSED(HttpStatus.FORBIDDEN, "J002", "그룹에 대한 권한이 없습니다."),
	GROUP_PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "J003", "그룹에 해당 맛집이 없습니다."),
	GROUP_INVITATION_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "J004", "그룹 초대 코드 저장에 실패했습니다."),
	GROUP_INVITATION_NOT_FOUND(HttpStatus.NOT_FOUND, "J005", "유효하지 않은 그룹 초대 코드입니다."),
	GROUP_MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "J006", "해당 그룹에 가입되어 있지 않습니다."),
	GROUP_ALREADY_SUBSCRIBED(HttpStatus.CONFLICT, "J007", "이미 구독 중인 그룹입니다."),
	SUBSCRIBE_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "J008", "구독 정보를 찾을 수 없습니다."),

	/**
	 * Elasticsearch(K000)
	 */
	ELASTICSEARCH_CONNECTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "K001", "Elasticsearch 연결 실패"),
	ELASTICSEARCH_INDEX_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "K002", "Elasticsearch 인덱스 생성 실패"),
	ELASTICSEARCH_INDEX_NOT_FOUND(HttpStatus.NOT_FOUND, "K003", "Elasticsearch 인덱스를 찾을 수 없음"),
	ELASTICSEARCH_QUERY_EXECUTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "K004", "Elasticsearch 쿼리 실행 실패"),
	ELASTICSEARCH_DOCUMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "K005", "Elasticsearch 문서를 찾을 수 없음"),
	ELASTICSEARCH_BULK_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "K006", "Elasticsearch 벌크 작업 실패"),

	/**
	 * Redis(L000)
	 */
	REDIS_CONNECTION_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "L001", "Redis 연결 실패"),
	REDIS_DATA_ACCESS_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "L002", "Redis 데이터 접근 실패"),
	REDIS_UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "L003", "알 수 없는 Redis 예외 발생"),

	/**
	 * Comment(M000)
	 */
	COMMENT_NOT_EXIST(HttpStatus.NOT_FOUND, "M001", "존재하지 않는 댓글입니다."),
	COMMENT_CREATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "M002", "댓글 생성 실패"),
	COMMENT_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "M003", "댓글 수정 실패"),
	COMMENT_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "M004", "댓글 삭제 실패"),

	/**
	 * Json(X000)
	 */
	JSON_SERIALIZATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "X001", "JSON으로 직렬화하는데 실패했습니다."),
	JSON_DESERIALIZATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "X002", "JSON을 역직렬화하는데 실패했습니다."),

	/**
	 * Webhook(Y000)
	 */
	JSON_PARSER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Y002", "Json Parser 오류"),
	CREATE_DISCORD_APPEND_MESSAGE_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "Y001", "디스코드 메시지 생성 실패"),
	DISCORD_SEND_MESSAGE_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "Y001", "디스코드 메시지 전송 실패"),
	MISSING_DISCORD_CONTENT_OR_EMBEDS(HttpStatus.BAD_REQUEST, "Y003", "디스코드 메시지 내용 또는 Embed 누락"),

	/**
	 * Etc(Z000)
	 */
	INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "Z001", "잘못된 타입 밸류"),
	UNDEFINED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Z002", "정의되지 않은 에러");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
