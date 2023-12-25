package com.safeway.userservice.exception;

public class UserAlreadyExistException extends BaseException {

    public UserAlreadyExistException(ErrorEnum errorCode) {
        super(errorCode);
    }

    public UserAlreadyExistException(ErrorEnum errorCode, Exception originalException) {
        super(errorCode, originalException);
    }
}
