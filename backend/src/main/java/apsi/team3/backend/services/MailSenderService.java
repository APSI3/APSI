package apsi.team3.backend.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {
    @Autowired
    private JavaMailSender emailSender;

    public void sendMailMessage(String to, String subject, String body) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        try {
            helper.setFrom("apsi.tickets@example.com");
            helper.setTo("mietowka06gmail.com");
            helper.setSubject("Ticket Information");
            String htmlContent = "<html><body><h1>Your Ticket Information</h1>" +
                    "<p>Please find your ticket QR code below:</p>" +
//                "<img src='data:image/png;base64," + qrCodeBase64 + "' alt='QR Code'>" +
                    "</body></html>";
            helper.setText(htmlContent, true);
        } catch (MessagingException e) {
            // todo: what now?
        }

        emailSender.send(message);
    }
}
