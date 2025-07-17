package com.woohakdong.framework.exception;

public record ErrorResponse(
        String message,
        String errorCode
) {
    /**
     * CustomException을 기반으로 ErrorResponse를 생성합니다.
     * @param exception
     * @return ErrorResponse
     */
    public static ErrorResponse of(CustomException exception) {
        return new ErrorResponse(
                exception.getMessage(),
                exception.getCode()
        );
    }

    /**
     * 직접 ErrorResponse를 생성합니다.
     * @param message
     * @param errorCode
     * @return ErrorResponse
     */
    public static ErrorResponse of(String message, String errorCode) {
        return new ErrorResponse(
                message,
                errorCode
        );
    }

    /**
     * 에러가 발생한 필드와 함께 ErrorResponse를 생성합니다.
     * @param message
     * @param errorCode
     * @param errorField
     * @return ErrorResponse
     */
    public static ErrorResponse of(String message, String errorCode, String errorField
    ) {
        return new ErrorResponse(
                String.format("%s: %s", message, errorField),
                errorCode
        );
    }
}
