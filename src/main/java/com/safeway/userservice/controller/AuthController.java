package com.safeway.userservice.controller;

import com.safeway.userservice.dto.request.ForgetPassword;
import com.safeway.userservice.dto.request.SignInRequest;
import com.safeway.userservice.dto.request.SignupRequest;
import com.safeway.userservice.dto.request.TokenRefreshRequest;
import com.safeway.userservice.dto.response.Response;
import com.safeway.userservice.dto.response.SignInResponse;
import com.safeway.userservice.entity.User;
import com.safeway.userservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                new Response(authService.loginUser(signInRequest),
                        "SF-200",
                        "Logged In Successfully",
                        HttpStatus.OK.toString()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        User user = authService.registerUser(signUpRequest);
        if (user != null && user.getId() > 0L) {
            SignInResponse signInResponse = authService.loginUser(new SignInRequest(signUpRequest.getUsername(), signUpRequest.getPassword()));
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response(signInResponse,
                    "SF-201",
                    "User Created Successfully",
                    HttpStatus.CREATED.toString()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(user,
                "SF-400",
                "Something Went Wrong",
                HttpStatus.BAD_REQUEST.toString()));

    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        authService.logoutUser();
        return ResponseEntity.ok("Log out successful!");
    }

    @PostMapping("/forget")
    public ResponseEntity<?> forgetPassword(@RequestBody ForgetPassword forgetPassword) {
        String value = authService.forgetPassword(forgetPassword.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(new Response(value,
                "SF-200",
                "Mail Sent Successfully",
                HttpStatus.OK.toString()));
    }

}