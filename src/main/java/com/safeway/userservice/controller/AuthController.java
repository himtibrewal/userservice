package com.safeway.userservice.controller;

import com.safeway.userservice.dto.request.ForgetPassword;
import com.safeway.userservice.dto.request.SignInRequest;
import com.safeway.userservice.dto.request.SignupRequest;
import com.safeway.userservice.dto.request.TokenRefreshRequest;
import com.safeway.userservice.dto.response.Response;
import com.safeway.userservice.dto.response.SignInResponse;
import com.safeway.userservice.entity.User;
import com.safeway.userservice.exception.BaseException;
import com.safeway.userservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.safeway.userservice.exception.ErrorEnum.ERROR_INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody SignInRequest signInRequest) {
        return ResponseEntity.ok(
                new Response(authService.loginUser(signInRequest), "Logged In Successfully"));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        User user = authService.registerUser(signUpRequest);
        if (user == null) {
            throw new BaseException(ERROR_INTERNAL_SERVER_ERROR);
        }
        SignInResponse signInResponse = authService.loginUser(new SignInRequest(signUpRequest.getEmail(), signUpRequest.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(signInResponse,
                "SF-201",
                "User Created Successfully",
                HttpStatus.CREATED.value()));


    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(new Response<>(authService.refreshToken(request), "Token regenerated !!"));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        authService.logoutUser();
        return ResponseEntity.ok(new Response<>(null, "Log out successful!"));
    }

    @PostMapping("/forget")
    public ResponseEntity<?> forgetPassword(@RequestBody ForgetPassword forgetPassword) {
        String value = authService.forgetPassword(forgetPassword.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(new Response(value, "Mail Sent Successfully"));
    }

}