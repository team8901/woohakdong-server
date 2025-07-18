package com.woohakdong.exception;

import static com.woohakdong.exception.CustomErrorInfo.BAD_REQUEST_INVALID_PARAMETER;
import static com.woohakdong.exception.CustomErrorInfo.INTERNAL_SERVER_ERROR;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        log.info("CustomException - reason : {}", ex.getMessage());

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(ErrorResponse.of(ex));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        log.info("MethodArgumentNotValidException - reason : {}", errorMessages);

        return ResponseEntity
                .status(BAD_REQUEST_INVALID_PARAMETER.getStatusCode())
                .body(ErrorResponse.of(
                        BAD_REQUEST_INVALID_PARAMETER.getMessage(),
                        BAD_REQUEST_INVALID_PARAMETER.getErrorCode(),
                        String.join(", ", errorMessages))
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {

        log.error("UnHandled Exception - reason : {}", ex.getMessage(), ex);

        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR.getStatusCode())
                .body(ErrorResponse.of(
                        INTERNAL_SERVER_ERROR.getMessage(),
                        INTERNAL_SERVER_ERROR.getErrorCode()
                ));
    }

}
