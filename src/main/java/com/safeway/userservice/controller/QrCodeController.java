package com.safeway.userservice.controller;

import com.safeway.userservice.dto.request.QrCodeGenerateRequest;
import com.safeway.userservice.service.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class QrCodeController {

    private final QrCodeService qrCodeService;

    @Autowired
    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }


    @PostMapping("/admin/qrcode")
    public ResponseEntity<?> generateQrCode(@RequestBody QrCodeGenerateRequest qrCodeGenerateRequest) {
        qrCodeService.generateQrCode(qrCodeGenerateRequest.getCount());
        return ResponseEntity.ok("Qr Code Generated !!");
    }


}
