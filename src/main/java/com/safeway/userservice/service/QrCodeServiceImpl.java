package com.safeway.userservice.service;

import com.safeway.userservice.entity.QrCodeEntity;
import com.safeway.userservice.exception.ErrorEnum;
import com.safeway.userservice.exception.NotFoundException;
import com.safeway.userservice.fastqrcodegen.QrCode;
import com.safeway.userservice.fastqrcodegen.QrSegment;
import com.safeway.userservice.repository.QrCodeRepository;
import com.safeway.userservice.sequrity.UserDetailsImpl;
import com.safeway.userservice.service.aws.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.safeway.userservice.utils.Commons.*;
import static com.safeway.userservice.utils.Commons.getUniqueString;

@Service
public class QrCodeServiceImpl implements QrCodeService {

    private static final String QR_CODE = "qrcode";

    private static final String FOLDER_QR_CODE = "qr-code";
    private final AmazonClient amazonClient;
    private final QrCodeRepository qrCodeRepository;

    @Autowired
    public QrCodeServiceImpl(QrCodeRepository qrCodeRepository, AmazonClient amazonClient) {
        this.qrCodeRepository = qrCodeRepository;
        this.amazonClient = amazonClient;
    }

    @Override
    public QrCodeEntity createQrCode() {
        QrCodeEntity qrCodeEntity = generateQrCodeModel();
        String s3Path = amazonClient.uploadFile(new File(qrCodeEntity.getImageDirLocal()), "qr-code", true);
        qrCodeEntity.setImageDirS3(s3Path);
        qrCodeEntity.setActive(true);
        return qrCodeRepository.save(qrCodeEntity);
    }

    @Override
    public QrCodeEntity reCreateQrCode(String key) {
        Optional<QrCodeEntity> qrCodeEntity = qrCodeRepository.findFirstByQrKey(key);
        if (!qrCodeEntity.isPresent()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "QR Code");
        }
        amazonClient.deleteFileFromS3Bucket(FOLDER_QR_CODE, qrCodeEntity.get().getImageDirS3());
        QrCodeEntity qrCodeEntityUpdate = generateQrCodeModel(qrCodeEntity.get());
        String s3Path = amazonClient.uploadFile(new File(qrCodeEntityUpdate.getImageDirLocal()), "qr-code", true);
        qrCodeEntityUpdate.setImageDirS3(s3Path);
        qrCodeEntityUpdate.setActive(true);
        return qrCodeRepository.save(qrCodeEntityUpdate);
    }

    @Override
    public QrCodeEntity getQrCodeByKey(String key) {
        Optional<QrCodeEntity> qrCodeEntity = qrCodeRepository.findFirstByQrKey(key);
        if (!qrCodeEntity.isPresent()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "QR Code");
        }
        return qrCodeEntity.get();
    }

    @Override
    public QrCodeEntity getQrCodeById(Long id) {
        Optional<QrCodeEntity> qrCodeEntity = qrCodeRepository.findById(id);
        if (!qrCodeEntity.isPresent()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "QR Code");
        }
        return qrCodeEntity.get();
    }

    @Override
    public QrCodeEntity qrCodeLinkToUser(String key, Long userId) {
        Optional<QrCodeEntity> qrCodeEntity = qrCodeRepository.findFirstByQrKey(key);
        if (!qrCodeEntity.isPresent()) {
            throw new NotFoundException(ErrorEnum.ERROR_NOT_FOUND, "QR Code");
        }

        QrCodeEntity qrCodeEntityUpdate = qrCodeEntity.get();
        qrCodeEntityUpdate.setUserId(userId);
        return qrCodeRepository.save(qrCodeEntityUpdate);
    }

    @Override
    public Optional<QrCodeEntity> getQrCodeByUserId(Long userId) {
        return qrCodeRepository.findAllByUserId(userId);
    }

    @Override
    public QrCodeEntity updateQrCode(Long id, QrCodeEntity qrCodeEntity) {
        return null;
    }

    private QrCodeEntity generateQrCodeModel() {
        Long userID = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        String key = getUniqueString();
        QrCodeEntity qrCodeEntity = new QrCodeEntity();
        qrCodeEntity.setBaseUrl(getBaseUrl());
        qrCodeEntity.setImageDirLocal(generateQrCode(key));
        qrCodeEntity.setQrKey(key);
        qrCodeEntity.setCreatedBy(userID);
        qrCodeEntity.setUpdatedBy(userID);
        qrCodeEntity.setCreatedOn(LocalDateTime.now());
        qrCodeEntity.setUpdatedOn(LocalDateTime.now());
        return qrCodeEntity;
    }

    private QrCodeEntity generateQrCodeModel(QrCodeEntity qrCodeEntity) {
        Long userID = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        qrCodeEntity.setBaseUrl(getBaseUrl());
        qrCodeEntity.setImageDirLocal(generateQrCode(qrCodeEntity.getQrKey()));
        qrCodeEntity.setUpdatedBy(userID);
        qrCodeEntity.setUpdatedOn(LocalDateTime.now());
        return qrCodeEntity;
    }


    private String generateQrCode(String key) {
        final String text = String.format("%s/%s/%s", getBaseUrl(), QR_CODE, key);
        String imagePath = String.format("%s/%s.%s", "images", key, "png");
        QrCode qr;
        List<QrSegment> segs;
        try {
            segs = QrSegment.makeSegments(text);
            qr = QrCode.encodeSegments(segs, QrCode.Ecc.HIGH, QrCode.MIN_VERSION, QrCode.MAX_VERSION, 3, true);  // Force mask 3
            writePng(toImage(qr, 8, 6, 0xFFE0E0, 0x602020), imagePath);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return imagePath;
    }

    private static void writePng(BufferedImage img, String filepath) throws IOException {
        ImageIO.write(img, "png", new File(filepath));
    }


    @Override
    public void deleteQrCode(Long id) {
        qrCodeRepository.deleteById(id);
    }


    private static BufferedImage toImage(com.safeway.userservice.fastqrcodegen.QrCode qr, int scale, int border, int lightColor, int darkColor) {
        Objects.requireNonNull(qr);
        if (scale <= 0 || border < 0)
            throw new IllegalArgumentException("Value out of range");
        if (border > Integer.MAX_VALUE / 2 || qr.size + border * 2L > Integer.MAX_VALUE / scale)
            throw new IllegalArgumentException("Scale or border too large");

        BufferedImage result = new BufferedImage((qr.size + border * 2) * scale, (qr.size + border * 2) * scale, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < result.getHeight(); y++) {
            for (int x = 0; x < result.getWidth(); x++) {
                boolean color = qr.getModule(x / scale - border, y / scale - border);
                result.setRGB(x, y, color ? darkColor : lightColor);
            }
        }
        return result;
    }
}
