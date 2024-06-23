package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.helpers.MailGenerator;
import apsi.team3.backend.helpers.QRCodeGenerator;
import apsi.team3.backend.model.MailStructure;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.google.zxing.WriterException;
import java.io.IOException;
import java.util.Map;

import static apsi.team3.backend.helpers.MailGenerator.generateTicket;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("$(spring.mail.username)")
    private String fromMail;

    @Async
    public void sendMail(String mail, MailStructure mailStructure) throws MessagingException, IOException, WriterException {
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(fromMail);
        helper.setSubject(mailStructure.getSubject());

        var byteResource = new ByteArrayResource(mailStructure.getQRCodeContent());

        var htmlContent = generateTicket(mailStructure.getMailContentParameters());
        helper.setText(htmlContent, true);
        helper.setTo(mail);
        helper.addAttachment("ticket.png", byteResource);

        mailSender.send(message);
    }

    public void sendTicketByEmail(String mailSubject, TicketDTO ticket) throws ApsiValidationException, WriterException, IOException {
        var event = ticket.getEvent();
        var ticketType = ticket.getTicketType();
        var user = ticket.getHolder();
        Map<String, String> ticketData =  Map.of(
            "eventName", event.getName(),
            "date", MailGenerator.getDateString(event.getStartDate(), event.getEndDate()),
            "ticketType", ticketType.getName(),
            "price", ticketType.getPrice().toString(),
            "userName", user.getLogin(),
            "holderFirstName", ticket.getHolderFirstName(),
            "holderLastName", ticket.getHolderLastName()
        );
        MailStructure mailStructure = new MailStructure(
            mailSubject,
            QRCodeGenerator.generateQRCodeByte(ticket.toJSON()),
            ticketData
        );

        try {
            sendMail(user.getEmail(), mailStructure);
        } catch (MessagingException | IOException | WriterException e) {
            throw new ApsiValidationException("Nie udało się wysłać maila z biletem", "mail");
        }

        String QRCode;
        try {
            QRCode = QRCodeGenerator.generateQRCodeBase64(ticket.toJSON());
            ticket.setQRCode(QRCode);
        }
        catch (WriterException|IOException e) {
            throw new ApsiValidationException(e);
        }
    }
}
