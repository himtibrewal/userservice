package com.safeway.userservice.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.safeway.userservice.dto.response.SignInResponse;
import com.safeway.userservice.entity.Permission;
import com.safeway.userservice.entity.RefreshToken;
import com.safeway.userservice.exception.ErrorEnum;
import com.safeway.userservice.exception.NotFoundException;
import com.safeway.userservice.repository.RefreshTokenRepository;
import com.safeway.userservice.exception.TokenRefreshException;
import com.safeway.userservice.sequrity.JwtUtils;
import com.safeway.userservice.sequrity.UserDetailsImpl;
import com.safeway.userservice.sequrity.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.safeway.userservice.utils.Commons.TOKEN_TYPE;


@Service
public class RefreshTokenService {
    @Value("${userservice.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final JwtUtils jwtUtils;

    private final  RefreshTokenRepository refreshTokenRepository;

    private final UserDetailsServiceImpl userDetailsService;


    @Autowired
    public RefreshTokenService(JwtUtils jwtUtils, RefreshTokenRepository refreshTokenRepository, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userDetailsService = userDetailsService;
    }

    public RefreshToken findByRefreshToken(String refToken) {
        Optional<RefreshToken> refreshToken =  refreshTokenRepository.getFirstByRefToken(refToken);
        if(refreshToken.isEmpty()){
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "authToken");
        }
        return refreshToken.get();
    }

    public RefreshToken createRefreshToken(Long userId, String token) {
        RefreshToken refreshToken = new RefreshToken();
        if (refreshTokenRepository.existsByUserId(userId)) {
            refreshToken = refreshTokenRepository.findByUserId(userId).get();
            refreshToken.setToken(null);
            refreshToken.setRefToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(refreshTokenDurationMs / 1000));
        } else {
            refreshToken.setToken(null);
            refreshToken.setRefToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(refreshTokenDurationMs / 1000));
            refreshToken.setUserId(userId);
        }
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public SignInResponse verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(LocalDateTime.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(ErrorEnum.ERROR_UNAUTHENTICATED);
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUserId(token.getUserId());
        String jwt = jwtUtils.generateJwtToken(userDetails);
        return SignInResponse.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .mobile(userDetails.getMobile())
                .accessToken(jwt)
                .refreshToken(token.getRefToken())
                .tokenType(TOKEN_TYPE)
                .roles(userDetails.getRoles())
                .permissions(userDetails.getPermissions())
                .build();
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}