package apsi.team3.backend.helpers;

import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.model.MailStructure;
import apsi.team3.backend.services.MailService;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.Map;

import static apsi.team3.backend.helpers.MailGenerator.getDateString;

public class MailSender {

    public static void sendTicketByEmail(
            MailService mailService,
            String mailSubject,
            TicketDTO ticket
           ) throws ApsiValidationException {
        var event = ticket.getEvent();
        var ticketType = ticket.getTicketType();
        var user = ticket.getHolder();
        Map<String, String> ticketData =  Map.of(
                "eventName", event.getName(),
                "date", getDateString(event.getStartDate(), event.getEndDate()),
                "ticketType", ticketType.getName(),
                "price", ticketType.getPrice().toString(),
                "userName", user.getLogin(),
                "holderFirstName", ticket.getHolderFirstName(),
                "holderLastName", ticket.getHolderLastName()
        );
        MailStructure mailStructure = new MailStructure(
                mailSubject,
                ticket.toJSON(),
                ticketData
        );
        try {
            mailService.sendMail(user.getEmail(), mailStructure);
        } catch (MessagingException | IOException | WriterException e) {
            throw new ApsiValidationException("Nie udało się wysłać maila z biletem", "mail");
        }

        String QRCode;
        try {
            QRCode = QRCodeGenerator.generateQRCode(ticket.toJSON());
            ticket.setQRCode(QRCode);
        }
        catch (WriterException|IOException e) {
            throw new ApsiValidationException(e);
        }
    }
}
