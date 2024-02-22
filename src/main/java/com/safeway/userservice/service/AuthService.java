package com.safeway.userservice.service;


import com.fasterxml.jackson.databind.util.JSONPObject;
import com.safeway.userservice.dto.request.TokenRefreshRequest;
import com.safeway.userservice.dto.request.SignInRequest;
import com.safeway.userservice.dto.request.SignupRequest;
import com.safeway.userservice.dto.response.SignInResponse;
import com.safeway.userservice.dto.response.SignupResponse;
import com.safeway.userservice.dto.response.TokenRefreshResponse;
import com.safeway.userservice.entity.User;

public interface AuthService {

    SignInResponse loginUser(SignInRequest signInRequest);

    User registerUser(SignupRequest signUpRequest);

    SignInResponse refreshToken(TokenRefreshRequest tokenRefreshRequest);

    void logoutUser();

    String forgetPassword(String email);


}
