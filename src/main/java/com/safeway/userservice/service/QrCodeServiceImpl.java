package com.safeway.userservice.service;

import com.safeway.userservice.entity.QrCode;
import com.safeway.userservice.fastqrcodegen.QrSegment;
import com.safeway.userservice.repository.QrCodeRepository;
import com.safeway.userservice.sequrity.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class QrCodeServiceImpl implements QrCodeService {

    private final QrCodeRepository qrCodeRepository;

    @Autowired
    public QrCodeServiceImpl(QrCodeRepository qrCodeRepository) {
        this.qrCodeRepository = qrCodeRepository;
    }

    @Override
    public Optional<QrCode> getQrCodeById(Long id) {
        return qrCodeRepository.findById(id);
    }

    @Override
    public Optional<QrCode> getQrCodeByUserId(Long userId) {
        return qrCodeRepository.findAllByUserId(userId);
    }

    @Override
    public QrCode updateQrCode(Long id, QrCode qrCode) {
        return null;
    }

    @Override
    public List<QrCode> generateQrCode(Integer count) {
        try {
            com.safeway.userservice.fastqrcodegen.QrCode qr;
            List<QrSegment> segs;
            segs = QrSegment.makeSegments("https://localhost:8080/qrcode/");
            qr = com.safeway.userservice.fastqrcodegen.QrCode.encodeSegments(segs, com.safeway.userservice.fastqrcodegen.QrCode.Ecc.HIGH, com.safeway.userservice.fastqrcodegen.QrCode.MIN_VERSION, com.safeway.userservice.fastqrcodegen.QrCode.MAX_VERSION, 3, true);  // Force mask 3

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(toImage(qr, 8, 6, 0xFFE0E0, 0x602020), "png", baos);
            byte[] bytes = baos.toByteArray();
            QrCode qrCode = new QrCode();
            qrCode.setImage(bytes);
          //  qrCode.setCreatedBy(userDetails.getId().intValue());
            qrCodeRepository.save(qrCode);


            writePng(toImage(qr, 8, 6, 0xFFE0E0, 0x602020), "project-nayuki-mask3-QR.png");
            return new ArrayList<>();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return new ArrayList<>();
    }

    private static void writePng(BufferedImage img, String filepath) throws IOException {
        ImageIO.write(img, "png", new File(filepath));
    }


    @Override
    public QrCode saveQrCode(QrCode qrCode) {
        return qrCodeRepository.save(qrCode);
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
