package com.woohakdong.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class CustomAuthException extends AuthenticationException {

    private final CustomErrorInfo customErrorInfo;

    public CustomAuthException(CustomErrorInfo customErrorInfo) {
        super(customErrorInfo.getMessage());
        this.customErrorInfo = customErrorInfo;
    }
}
