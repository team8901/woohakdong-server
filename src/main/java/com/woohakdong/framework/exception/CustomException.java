package com.woohakdong.framework.exception;

public class CustomException extends RuntimeException {
    private final CustomErrorInfo errorInfo;

    public CustomException(CustomErrorInfo errorInfo) {
        super(errorInfo.getMessage());
        this.errorInfo = errorInfo;
    }

    public Integer getStatusCode() {
        return errorInfo.getStatusCode();
    }

    public String getCode() {
        return errorInfo.getErrorCode();
    }
}
