package com.safeway.userservice.dto.response;

public class SignupResponse {
    private String username;
    private String message;

    public SignupResponse(String username, String message) {
        this.username = username;
        this.message = message;
    }
}
