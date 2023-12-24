package com.safeway.userservice.service;

import com.safeway.userservice.dto.EmailDetails;
import com.safeway.userservice.dto.request.SignInRequest;
import com.safeway.userservice.dto.request.SignupRequest;
import com.safeway.userservice.dto.request.TokenRefreshRequest;
import com.safeway.userservice.dto.response.SignInResponse;
import com.safeway.userservice.dto.response.TokenRefreshResponse;
import com.safeway.userservice.entity.RefreshToken;
import com.safeway.userservice.entity.User;
import com.safeway.userservice.repository.UserRepository;
import com.safeway.userservice.sequrity.JwtUtils;
import com.safeway.userservice.exception.TokenRefreshException;
import com.safeway.userservice.sequrity.UserDetailsImpl;
import com.safeway.userservice.service.admin.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.safeway.userservice.utils.Commons.generatePassword;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final PasswordEncoder passwordEncoder;

    private final RefreshTokenService refreshTokenService;

    private final EmailService emailService;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils, PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService, EmailService emailService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public SignInResponse loginUser(SignInRequest signInRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
       // SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId(), jwt);

        return new SignInResponse(userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), userDetails.getMobile(), jwt, refreshToken.getRefToken(), "Bearer", roles);

    }

    @Override
    public User registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new TokenRefreshException("test", "username Already exist");
        }
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getMobile(), passwordEncoder.encode(signUpRequest.getPassword()));

        // add default country code
        user.setCountryCode("+91");
        return userRepository.save(user);
    }

    @Override
    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) throws TokenRefreshException {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByRefreshToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(rt -> userRepository.findById(rt.getUserId()).get())
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUserId(String.valueOf(user.getId()));
                    return new TokenRefreshResponse(token, requestRefreshToken, "Bearer");
                }).orElseThrow(() -> {
                    throw new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!");
                });
    }

    @Override
    public void logoutUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        refreshTokenService.deleteByUserId(userId);
    }

    @Override
    public String forgetPassword(String email) {
        Optional<User> user = userRepository.findFirstByEmail(email);
        if (!user.isPresent()) {
            throw new RuntimeException("User not Available");
        }
        String password = generatePassword();
        String subject = "[SAFEWAY] Forget Password";
        String body = "Your New Password is " + password;
        userRepository.updateUserPasswordById(passwordEncoder.encode(password), user.get().getId());
        return emailService.sendSimpleMail(new EmailDetails(email, body, subject));
    }
}
