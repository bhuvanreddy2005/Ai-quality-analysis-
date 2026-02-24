package com.crop.cropanalysis.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

public class QRCodeGenerator {

    public static String generateQRCode(String text, Long id) throws Exception {

        String qrDir = System.getProperty("user.dir") + "/src/main/resources/static/qrcodes/";

        File dir = new File(qrDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = qrDir + "crop_" + id + ".png";

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300);

        Path path = Paths.get(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        return "http://localhost:8080/qrcodes/crop_" + id + ".png";
    }
}
