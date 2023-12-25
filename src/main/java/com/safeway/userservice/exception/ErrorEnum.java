package com.safeway.userservice.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public enum ErrorEnum {

    /**
     * common errors
     */

    ERROR_UNAUTHENTICATED("SW-401", "invalid User !! Please Login first", HttpStatus.UNAUTHORIZED),
    ERROR_EXPIRED_JWT_TOKEN("SW-401", "Authorization header is invalid", HttpStatus.UNAUTHORIZED),
    ERROR_BAD_REQUEST("SW-400", "Invalid Request", HttpStatus.BAD_REQUEST),

    ERROR_NOT_FOUND("SW-404", "Not Found", HttpStatus.NOT_FOUND),

    ERROR_USER_ALREADY_AVAILABLE("SW-400", "User Already Registered !", HttpStatus.BAD_REQUEST),

    ERROR_FORBIDDEN("SW-403", "You don't have permission", HttpStatus.FORBIDDEN),

    ERROR_NO_ERROR("SW-200", "NO Error", HttpStatus.OK),

    ERROR_ENUM("", "", HttpStatus.OK),
    ERROR_INTERNAL_SERVER_ERROR("SW-500", "Internal Server Error", INTERNAL_SERVER_ERROR),

    ERROR_SQS_CONNECTION("SW-508", "HA-SQS can't connect to SQS", INTERNAL_SERVER_ERROR);

    private final String code;

    private final String message;

    private final HttpStatus httpStatus;

    ErrorEnum(String errorCode, String message, HttpStatus httpStatus) {
        this.code = errorCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}