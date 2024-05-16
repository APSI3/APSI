package apsi.team3.backend.services;

import apsi.team3.backend.model.MailStructure;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

import java.io.ByteArrayOutputStream;

import static apsi.team3.backend.helpers.MailGenerator.generateTicket;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("$(spring.mail.username)")
    private String fromMail;

    public void sendMail(String mail, MailStructure mailStructure) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(fromMail);
        helper.setSubject(mailStructure.getSubject());

        ByteArrayOutputStream outputStream = QRCode.from(mailStructure.getAttachment()).to(ImageType.PNG).stream();
        byte[] qrCodeBytes = outputStream.toByteArray();
        ByteArrayResource qrCodeResource = new ByteArrayResource(qrCodeBytes);

        String htmlContent = generateTicket(mailStructure.getTicketData());
        helper.setText(htmlContent, true);
        helper.setTo(mail);
        helper.addAttachment("ticket.png", qrCodeResource);

        mailSender.send(message);
    }
}
