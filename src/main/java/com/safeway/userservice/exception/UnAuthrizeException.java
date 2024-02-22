package com.safeway.userservice.exception;


public class UnAuthrizeException extends BaseException {

    private String message;

    public UnAuthrizeException(ErrorEnum errorCode) {
        super(errorCode);
    }

    public UnAuthrizeException(ErrorEnum errorCode, String message) {
        super(errorCode);
        this.message = message;
    }

    public UnAuthrizeException(ErrorEnum errorCode, Exception originalException) {
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

