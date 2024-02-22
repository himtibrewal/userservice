package com.safeway.userservice.service;

import com.safeway.userservice.dto.EmailDetails;
import com.safeway.userservice.dto.request.SignInRequest;
import com.safeway.userservice.dto.request.SignupRequest;
import com.safeway.userservice.dto.request.TokenRefreshRequest;
import com.safeway.userservice.dto.response.SignInResponse;
import com.safeway.userservice.entity.RefreshToken;
import com.safeway.userservice.entity.User;
import com.safeway.userservice.sequrity.JwtUtils;
import com.safeway.userservice.sequrity.UserDetailsImpl;
import com.safeway.userservice.service.admin.EmailService;
import com.safeway.userservice.service.admin.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.safeway.userservice.utils.Commons.TOKEN_TYPE;
import static com.safeway.userservice.utils.Commons.generatePassword;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    private final RoleService roleService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final PasswordEncoder passwordEncoder;

    private final RefreshTokenService refreshTokenService;

    private final EmailService emailService;

    @Autowired
    public AuthServiceImpl(UserService userService, RoleService roleService, AuthenticationManager authenticationManager, JwtUtils jwtUtils, PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService, EmailService emailService) {
        this.userService = userService;
        this.roleService = roleService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
        this.emailService = emailService;
    }

    @Override
    public SignInResponse loginUser(SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest, signInRequest.getPassword()));
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId(), jwt);
        userService.updateMobileData(userDetails.getId(), signInRequest.getRegKey(), signInRequest.getDeviceKey());
        return SignInResponse.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .mobile(userDetails.getMobile())
                .accessToken(jwt)
                .refreshToken(refreshToken.getRefToken())
                .tokenType(TOKEN_TYPE)
                .roles(userDetails.getRoles())
                .permissions(userDetails.getPermissions())
                .build();

    }

    @Override
    public SignInResponse refreshToken(TokenRefreshRequest request) {
        RefreshToken refreshToken = refreshTokenService.findByRefreshToken(request.getRefreshToken());
        return refreshTokenService.verifyExpiration(refreshToken);
    }

    @Override
    public User registerUser(SignupRequest signUpRequest) {
//        if (userService.existsByEmailOrMobile(signUpRequest.getEmail(), signUpRequest.getMobile())) {
//            throw new UserAlreadyExistException(ERROR_USER_ALREADY_AVAILABLE);
//        }
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .mobile(signUpRequest.getMobile())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .build();

        return null; //userService.saveUser(user);
    }


    @Override
    public void logoutUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        SecurityContextHolder.getContext().setAuthentication(null);
        refreshTokenService.deleteByUserId(userId);
    }

    @Override
    public String forgetPassword(String email) {
        User user = userService.findUserByEmail(email);
        String password = generatePassword();
        String subject = "[SAFEWAY] Forget Password";
        String body = "Your New Password is " + password;
        userService.updatePassword(user.getId(), passwordEncoder.encode(password));
        return emailService.sendSimpleMail(new EmailDetails(email, body, subject));
    }
}
