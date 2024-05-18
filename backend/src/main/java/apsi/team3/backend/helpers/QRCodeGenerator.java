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
    public static String generateQRCode(String ticketInfo) throws WriterException, IOException {
        int width = 300;
        int height = 300;
        String format = "png";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Configure QR code parameters
        com.google.zxing.Writer qrWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrWriter.encode(ticketInfo, BarcodeFormat.QR_CODE, width, height);

        // Write QR code to output stream
        MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream);

        // Convert output stream to base64 string
        byte[] qrBytes = outputStream.toByteArray();
        String base64QRCode = Base64.getEncoder().encodeToString(qrBytes);

        return base64QRCode;
    }
}
