package com.safeway.userservice.service;

import com.safeway.userservice.entity.QrCodeEntity;

import java.util.Optional;

public interface QrCodeService {
    QrCodeEntity createQrCode();

    QrCodeEntity getQrCodeByKey(String key);

    QrCodeEntity reCreateQrCode(String key);

    QrCodeEntity qrCodeLinkToUser(String key, Long userId);

    QrCodeEntity getQrCodeById(Long id);


    Optional<QrCodeEntity> getQrCodeByUserId(Long userId);

    QrCodeEntity updateQrCode(Long id, QrCodeEntity qrCodeEntity);

    void deleteQrCode(Long id);
}
