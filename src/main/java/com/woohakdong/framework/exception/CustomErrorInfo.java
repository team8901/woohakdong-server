package com.woohakdong.framework.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 400 : 요청 자체가 잘못되었을 때 ( 클라이언트가 이상한 값을 보내는 경우 )
 * 401 : 인증이 필요한 요청이지만, 인증되지 않았을 때 ( 클라이언트가 인증되지 않은 경우 )
 * 403 : 인증은 되었지만, 권한이 없을 때 ( 클라이언트가 인증되었지만, 해당 리소스에 대한 권한이 없는 경우 )
 * 404 : 요청한 리소스가 존재하지 않을 때 ( 클라이언트가 잘못된 URL 또는 id에 대한 것을 요청하는 경우 )
 * 409 : 요청이 현재 상태와 충돌할 때 ( 요청 자체는 올바르지만, 현재 상태와 충돌하는 경우 )
 * 500 : 서버 내부 오류가 발생했을 때 ( 서버에서 예기치 않은 오류가 발생한 경우 )
 */
@RequiredArgsConstructor
@Getter
public enum CustomErrorInfo {
    // 400 Bad Request
    BAD_REQUEST_INVALID_PARAMETER(400, "잘못된 요청입니다.", "400001"),

    // 401 Unauthorized
    UNAUTHORIZED_INVALID_TOKEN(401, "유효하지 않은 토큰입니다.", "401001"),
    UNAUTHORIZED_EXPIRED_TOKEN(401, "만료된 토큰입니다.", "401002"),
    UNAUTHORIZED_NO_TOKEN(401, "인증 토큰이 없습니다.", "401003"),

    // 404 Not Found
    NOT_FOUND_USER_PROFILE(404, "해당 유저 프로필을 찾을 수 없습니다.", "404001"),
    NOT_FOUND_CLUB(404, "해당 동아리를 찾을 수 없습니다.", "404002"),


    // 409 Conflict
    CONFLICT_ALREADY_EXISTING_USER_PROFILE(409, "이미 존재하는 유저 프로필입니다.", "409001"),
    CONFLICT_ALREADY_EXISTING_CLUB_NAME_EN(409, "이미 존재하는 클럽 영문 이름입니다.", "409002"),
    CONFLICT_ALREADY_JOINED_CLUB(409, "이미 가입한 동아리입니다.", "409003"),


    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생했습니다.", "500001")
    ;

    private final Integer statusCode;
    private final String message;
    private final String errorCode;
}
