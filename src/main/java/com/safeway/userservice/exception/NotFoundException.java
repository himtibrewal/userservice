package com.safeway.userservice.exception;

public class NotFoundException extends BaseException {

    private String message;

    public NotFoundException(ErrorEnum errorCode) {
        super(errorCode);
    }

    public NotFoundException(ErrorEnum errorCode, String message) {
        super(errorCode);
        this.message = message;
    }

    public NotFoundException(ErrorEnum errorCode, Exception originalException) {
        super(errorCode, originalException);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
