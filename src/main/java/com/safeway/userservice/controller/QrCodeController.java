package com.safeway.userservice.controller;

import com.safeway.userservice.dto.response.Response;
import com.safeway.userservice.entity.QrCodeEntity;
import com.safeway.userservice.exception.BaseException;
import com.safeway.userservice.sequrity.UserDetailsImpl;
import com.safeway.userservice.service.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class QrCodeController extends BaseController{

    private final QrCodeService qrCodeService;

    @Autowired
    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }


    @PostMapping("/qrcode")
    public ResponseEntity<?> generateQrCode() {
        QrCodeEntity qrCodeEntity = qrCodeService.createQrCode();
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(qrCodeEntity,
                "SF-201",
                "QR Code  Created Successfully",
                HttpStatus.CREATED.value()));
    }

    @GetMapping("/qrcode/{key}")
    public ResponseEntity<?> getQrCode(@PathVariable String key) {
        return ok(new Response<QrCodeEntity>(qrCodeService.getQrCodeByKey(key),
                "SF-200",
                "QR Code Found Successfully",
                HttpStatus.OK.value()));
    }

    @PutMapping("/qrcode/{key}")
    public ResponseEntity<?> reCreateQrCode(@PathVariable String key) {
        return ok(new Response<QrCodeEntity>(qrCodeService.reCreateQrCode(key),
                "SF-200",
                "QR Code Found Successfully",
                HttpStatus.OK.value()));
    }

    @GetMapping("/qrcode/{key}/user")
    public ResponseEntity<?> scanQRCode(@PathVariable String key) {
        Long userID = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return ok(new Response<QrCodeEntity>(qrCodeService.qrCodeLinkToUser(key, userID),
                "SF-200",
                "QR Code Mapped Successfully",
                HttpStatus.OK.value()));
    }

    @GetMapping("/qrcode/{key}/user/{id}")
    public ResponseEntity<?> mapQRCode(@PathVariable String key, @PathVariable Long id) {
        return ok(new Response<QrCodeEntity>(qrCodeService.qrCodeLinkToUser(key, id),
                "SF-200",
                "QR Code Mapped Successfully",
                HttpStatus.OK.value()));
    }

}
