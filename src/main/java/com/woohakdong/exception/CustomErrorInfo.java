package com.woohakdong.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 400 : 요청 자체가 잘못되었을 때 ( 클라이언트가 이상한 값을 보내는 경우 )
 * <p>
 * 401 : 인증이 필요한 요청이지만, 인증되지 않았을 때 ( 클라이언트가 인증되지 않은 경우 )
 * <p>
 * 403 : 인증은 되었지만, 권한이 없을 때 ( 클라이언트가 인증되었지만, 해당 리소스에 대한 권한이 없는 경우 )
 * <p>
 * 404 : 요청한 리소스가 존재하지 않을 때 ( 클라이언트가 잘못된 URL 또는 id에 대한 것을 요청하는 경우 )
 * <p>
 * 409 : 요청이 현재 상태와 충돌할 때 ( 요청 자체는 올바르지만, 현재 상태와 충돌하는 경우 )
 * <p>
 * 500 : 서버 내부 오류가 발생했을 때 ( 서버에서 예기치 않은 오류가 발생한 경우 )
 */
@RequiredArgsConstructor
@Getter
public enum CustomErrorInfo {
    // 400 Bad Request
    BAD_REQUEST_INVALID_PARAMETER(400, "잘못된 요청입니다.", "400001"),
    BAD_REQUEST_FIREBASE_TOKEN(400, "잘못된 firebase 토큰입니다.", "400002"),

    // 401 Unauthorized
    UNAUTHORIZED_INVALID_TOKEN(401, "유효하지 않은 토큰입니다.", "401001"),
    UNAUTHORIZED_EXPIRED_TOKEN(401, "만료된 토큰입니다.", "401002"),
    UNAUTHORIZED_NO_TOKEN(401, "인증 토큰이 없습니다.", "401003"),

    // 403 Forbidden
    FORBIDDEN_UNAUTHORIZED(403, "접근 권한이 없습니다.", "403001"),
    FORBIDDEN_CLUB_OWNER_ONLY(403, "동아리 소유자만 접근할 수 있습니다.", "403002"),


    // 404 Not Found
    NOT_FOUND_USER_PROFILE(404, "해당 유저 프로필을 찾을 수 없습니다.", "404001"),
    NOT_FOUND_CLUB(404, "해당 동아리를 찾을 수 없습니다.", "404002"),
    NOT_FOUND_CLUB_MEMBERSHIP(404, "해당 유저의 동아리 맴버십을 찾을 수 없습니다.", "404003"),
    NOT_FOUND_CLUB_APPLICATION_FORM(404, "해당 동아리의 신청 양식을 찾을 수 없습니다.", "404004"),
    NOT_FOUND_NOTICE(404, "해당 공지사항을 찾을 수 없습니다.", "404005"),
    NOT_FOUND_CLUB_ITEM(404, "해당 물품을 찾을 수 없습니다.", "404006"),
    NOT_FOUND_CLUB_ITEM_HISTORY(404, "해당 대여 내역을 찾을 수 없습니다.", "404007"),


    // 409 Conflict
    CONFLICT_ALREADY_EXISTING_USER_PROFILE(409, "이미 존재하는 유저 프로필입니다.", "409001"),
    CONFLICT_ALREADY_EXISTING_CLUB_NAME(409, "이미 존재하는 동아리 이름입니다.", "409002"),
    CONFLICT_ALREADY_JOINED_CLUB(409, "이미 가입한 동아리입니다.", "409003"),
    CONFLICT_ITEM_NOT_AVAILABLE(409, "해당 물품은 대여할 수 없는 상태입니다.", "409004"),
    CONFLICT_ITEM_ALREADY_RENTED(409, "해당 물품은 이미 대여 중입니다.", "409005"),
    CONFLICT_RENTAL_DAYS_EXCEEDED(409, "최대 대여 일수를 초과했습니다.", "409006"),
    CONFLICT_ITEM_NOT_RENTED(409, "해당 물품은 대여 중이 아닙니다.", "409007"),


    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생했습니다.", "500001"),
    ;

    private final Integer statusCode;
    private final String message;
    private final String errorCode;
}
