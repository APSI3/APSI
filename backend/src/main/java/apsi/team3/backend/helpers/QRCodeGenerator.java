package apsi.team3.backend.helpers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeGenerator {
    public static String generateQRCodeBase64(String ticketInfo) throws WriterException, IOException {
        byte[] qrBytes = generateQRCodeByte(ticketInfo);
        return convertQRCodeByteToBase64(qrBytes);
    }

    public static String convertQRCodeByteToBase64(byte[] qrBytes) {
        return Base64.getEncoder().encodeToString(qrBytes);
    }

    public static byte[] generateQRCodeByte(String ticketInfo) throws WriterException, IOException {
        int width = 300;
        int height = 300;
        String format = "png";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Configure QR code parameters
        com.google.zxing.Writer qrWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrWriter.encode(ticketInfo, BarcodeFormat.QR_CODE, width, height);

        // Write QR code to output stream
        MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream);
        return outputStream.toByteArray();
    }
}
