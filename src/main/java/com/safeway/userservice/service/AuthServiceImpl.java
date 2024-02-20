package com.safeway.userservice.service;

import com.safeway.userservice.dto.EmailDetails;
import com.safeway.userservice.dto.request.SignInRequest;
import com.safeway.userservice.dto.request.SignupRequest;
import com.safeway.userservice.dto.request.TokenRefreshRequest;
import com.safeway.userservice.dto.response.SignInResponse;
import com.safeway.userservice.dto.response.TokenRefreshResponse;
import com.safeway.userservice.entity.RefreshToken;
import com.safeway.userservice.entity.User;
import com.safeway.userservice.exception.ErrorEnum;
import com.safeway.userservice.sequrity.JwtUtils;
import com.safeway.userservice.exception.TokenRefreshException;
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
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId(), jwt);
        return new SignInResponse(userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(),
                userDetails.getMobile(), jwt, refreshToken.getRefToken(), TOKEN_TYPE, userDetails.getRoles(),
                userDetails.getPermissions());

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
                .countryCode("+91")
                .build();

        return null; //userService.saveUser(user);
    }

    @Override
    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) throws TokenRefreshException {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByRefreshToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
//                .map(rt -> userService.getUserById(rt.getUserId()))
                .map(user -> {
                    String token =  null; // jwtUtils.generateTokenFromUserId(String.valueOf(user.getId()));
                    return new TokenRefreshResponse(token, requestRefreshToken, "Bearer");
                })
                .orElseThrow(() -> {
                    throw new TokenRefreshException(ErrorEnum.ERROR_BAD_REQUEST);
                });
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
        Optional<User> user =  null ;///userService.findUserByEmail(email);
        if (!user.isPresent()) {
            throw new RuntimeException("User not Available");
        }
        String password = generatePassword();
        String subject = "[SAFEWAY] Forget Password";
        String body = "Your New Password is " + password;
       // userService.updateUserPasswordById(passwordEncoder.encode(password), user.get().getId());
        return emailService.sendSimpleMail(new EmailDetails(email, body, subject));
    }
}
