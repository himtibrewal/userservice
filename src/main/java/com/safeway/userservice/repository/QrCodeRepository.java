package com.safeway.userservice.repository;

import com.safeway.userservice.entity.QrCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCodeEntity, Long> {

    Optional<QrCodeEntity> findAllByUserId(Long userId);

    Optional<QrCodeEntity> findFirstByQrKey(String key);
}
