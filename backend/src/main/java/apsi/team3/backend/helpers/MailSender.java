package apsi.team3.backend.helpers;

import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.exceptions.ApsiException;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.model.MailStructure;
import apsi.team3.backend.services.MailService;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import org.springframework.security.core.parameters.P;

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
            mailService.sendTicketMail(user.getEmail(), mailStructure);
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

    public static void sendTicketDeletedEmail(
            MailService mailService,
            TicketDTO ticket
    ) throws ApsiException {
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
            mailService.sendMail(user.getEmail(), mailSubject, message);
        } catch (MessagingException  e) {
            throw new ApsiException("Nie udało się wysłać maila ze zwrotem pieniędzy");
        }
    }
}
