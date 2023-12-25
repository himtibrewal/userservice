package com.safeway.userservice.exception;

public class TokenRefreshException extends BaseException {

    public TokenRefreshException(ErrorEnum errorCode) {
        super(errorCode);
    }

    public TokenRefreshException(ErrorEnum errorCode, Exception originalException) {
        super(errorCode, originalException);
    }
}