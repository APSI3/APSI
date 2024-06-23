package apsi.team3.backend.services;

import apsi.team3.backend.model.MailStructure;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static apsi.team3.backend.helpers.MailGenerator.generateTicket;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("$(spring.mail.username)")
    private String fromMail;

    @Async
    public void sendTicketMail(String mail, MailStructure mailStructure) throws MessagingException, IOException, WriterException {
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(fromMail);
        helper.setSubject(mailStructure.getSubject());

        var width = 300;
        var height = 300;
        var format = "png";
        var outputStream = new ByteArrayOutputStream();

        // Configure QR code parameters
        var qrWriter = new QRCodeWriter();
        var bitMatrix = qrWriter.encode(mailStructure.getQRCodeContent(), BarcodeFormat.QR_CODE, width, height);
        // Write QR code to output stream
        MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream);
        var byteResource = new ByteArrayResource(outputStream.toByteArray());

        var htmlContent = generateTicket(mailStructure.getMailContentParameters());
        helper.setText(htmlContent, true);
        helper.setTo(mail);
        helper.addAttachment("ticket.png", byteResource);

        mailSender.send(message);
    }

    @Async
    public void sendMail(String mail, String mailSubject, String mailMessage) throws MessagingException {
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromMail);
        helper.setSubject(mailSubject);
        helper.setText(mailMessage);
        helper.setTo(mail);

        mailSender.send(message);
    }
}
