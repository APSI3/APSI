package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.exceptions.ApsiException;
import apsi.team3.backend.exceptions.ApsiValidationException;
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
import java.util.HashMap;
import java.util.Map;

import static apsi.team3.backend.helpers.MailGenerator.generateTicket;
import static apsi.team3.backend.helpers.MailGenerator.getDateString;
import static apsi.team3.backend.helpers.MailGenerator.getTimeString;

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

        var byteResource = new ByteArrayResource(mailStructure.getQRCodeContent());

        var htmlContent = generateTicket(mailStructure.getMailContentParameters());
        helper.setText(htmlContent, true);
        helper.setTo(mail);
        helper.addAttachment("ticket.png", byteResource);

        mailSender.send(message);
    }

    public void sendTicketByEmail(String mailSubject, TicketDTO ticket) throws ApsiValidationException, WriterException, IOException {
        var event = ticket.getEvent();
        var location = event.getLocation();
        var ticketType = ticket.getTicketType();
        var user = ticket.getHolder();
        Map<String, String> ticketData = new HashMap<>();

        ticketData.put("eventName", event.getName());
        ticketData.put("dateFrom", getDateString(event.getStartDate()));
        ticketData.put("dateTo", getDateString(event.getEndDate()));
        ticketData.put("timeFrom", event.getStartTime() != null ? getTimeString(event.getStartTime()) : "");
        ticketData.put("timeTo", event.getEndTime() != null ? getTimeString(event.getEndTime()) : "");

        if (location != null) {
            ticketData.put("description", location.getDescription());
            ticketData.put("street", location.getStreet());
            ticketData.put("number", "");
            ticketData.put("zipCode", location.getZip_code());
            ticketData.put("city", location.getCity());
        }

        ticketData.put("ticketType", ticketType.getName());
        ticketData.put("price", ticketType.getPrice().toString());
        ticketData.put("userName", user.getLogin());
        ticketData.put("holderFirstName", ticket.getHolderFirstName());
        ticketData.put("holderLastName", ticket.getHolderLastName());

        MailStructure mailStructure = new MailStructure(
            mailSubject,
            QRCodeGenerator.generateQRCodeByte(ticket.toJSON()),
            ticketData
        );

        try {
            sendTicketMail(user.getEmail(), mailStructure);
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

    public void sendTicketDeletedEmail(TicketDTO ticket) throws ApsiException {
        var user = ticket.getHolder();
        var event = ticket.getEvent();
        String eventUrl = String.format("localhost:3000/event/%s", event.getId());
        String mailSubject = "Kupiony przez Ciebie bilet został anulowany";
        String message = String.format("""
                Przepraszamy,
                Organizator wydarzenia %s anulował twój typ biletu. Wysłaliśmy %s zł na Twoje konto.
                Aby wziąć udział w wydarzeniu, sprawdź czy dostępne są inne bilety na stronie: %s.
                """, event.getName(), ticket.getTicketType().getPrice(), eventUrl);
        try {
            sendMail(user.getEmail(), mailSubject, message);
        } catch (MessagingException  e) {
            throw new ApsiException("Nie udało się wysłać maila ze zwrotem pieniędzy");
        }
    }
}
