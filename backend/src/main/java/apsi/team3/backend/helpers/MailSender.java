package apsi.team3.backend.helpers;

import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.model.MailStructure;
import apsi.team3.backend.services.MailService;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static apsi.team3.backend.helpers.MailGenerator.getDateString;
import static apsi.team3.backend.helpers.MailGenerator.getTimeString;

public class MailSender {

    public static void sendTicketByEmail(
            MailService mailService,
            String mailSubject,
            TicketDTO ticket
           ) throws ApsiValidationException {
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
