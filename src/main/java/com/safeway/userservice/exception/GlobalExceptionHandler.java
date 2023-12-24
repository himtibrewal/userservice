package com.safeway.userservice.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
class GlobalExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ExpiredJwtException.class)
    ResponseEntity processExpiredJwtException(ExpiredJwtException ex) {
        return generateErrorResponse(ErrorEnum.ERROR_EXPIRED_JWT_TOKEN, ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity processAuthenticationException(AuthenticationException ex) {
        logger.error("final error:: {}", ex.getMessage());
        return generateErrorResponse(ErrorEnum.ERROR_UNAUTHENTICATED, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    ResponseEntity<ErrorResponse> processUnHandleException(Exception ex) {
        logger.error("final error:: {}", ex.getMessage());
        return generateErrorResponse(ErrorEnum.ERROR_INTERNAL_SERVER_ERROR, "Something went wrong");
    }

    private ResponseEntity<ErrorResponse> generateErrorResponse(ErrorEnum errorCode, String message) {
        ErrorResponse errorResponse = new ErrorResponse(null, errorCode.getErrorCode(), errorCode.getMessage(), message);
        return new ResponseEntity<ErrorResponse>(errorResponse, errorCode.getHttpStatus());
    }
}