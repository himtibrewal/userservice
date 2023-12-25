package com.safeway.userservice.exception;

public class BaseException extends RuntimeException {
    private ErrorEnum errorCode;
    private Exception originalException;

    public BaseException(ErrorEnum errorCode) {
        this.errorCode = errorCode;
    }

    public BaseException(ErrorEnum errorCode, Exception originalException) {
        this.errorCode = errorCode;
        this.originalException = originalException;
    }

    public ErrorEnum getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorEnum errorCode) {
        this.errorCode = errorCode;
    }

    public Exception getOriginalException() {
        return originalException;
    }

    public void setOriginalException(Exception originalException) {
        this.originalException = originalException;
    }
}
