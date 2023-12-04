package com.safeway.userservice.service;

import com.safeway.userservice.entity.QrCode;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface QrCodeService {
    Optional<QrCode> getQrCodeById(Long id);

    Optional<QrCode> getQrCodeByUserId(Long userId);

    QrCode updateQrCode(Long id, QrCode qrCode);

    QrCode saveQrCode(QrCode qrCode);

    List<QrCode> generateQrCode(Integer count);

    void deleteQrCode(Long id);
}
