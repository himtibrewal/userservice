package com.safeway.userservice.exception;


public class UnAuthorizeException extends BaseException {

    private String message;

    public UnAuthorizeException(ErrorEnum errorCode) {
        super(errorCode);
    }

    public UnAuthorizeException(ErrorEnum errorCode, String message) {
        super(errorCode);
        this.message = message;
    }

    public UnAuthorizeException(ErrorEnum errorCode, Exception originalException) {
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

