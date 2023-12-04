package com.safeway.userservice.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.safeway.userservice.entity.RefreshToken;
import com.safeway.userservice.repository.RefreshTokenRepository;
import com.safeway.userservice.exception.TokenRefreshException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RefreshTokenService {
    @Value("${userservice.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private RefreshTokenRepository refreshTokenRepository;


    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public Optional<RefreshToken> findByRefreshToken(String refToken) {
        return refreshTokenRepository.findAllByRefToken(refToken);
    }

    public RefreshToken createRefreshToken(Long userId, String token) {
        RefreshToken refreshToken = new RefreshToken();
        if (refreshTokenRepository.existsByUserId(userId)) {
            refreshToken = refreshTokenRepository.findByUserId(userId).get();
            refreshToken.setToken(token);
            refreshToken.setRefToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(refreshTokenDurationMs / 1000));
        } else {
            refreshToken.setToken(token);
            refreshToken.setRefToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(refreshTokenDurationMs / 1000));
            refreshToken.setUserId(userId);
            refreshTokenRepository.save(refreshToken);
        }
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) throws TokenRefreshException {
        if (token.getExpiryDate().compareTo(LocalDateTime.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}