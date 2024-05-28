package apsi.team3.backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = false)
@Getter
@AllArgsConstructor
@Setter
public class TicketDTO {
    Long id;
    TicketTypeDTO ticketType;
    UserDTO holder;
    EventDTO event;
    LocalDate purchaseDate;
    String QRCode;
    String holderFirstName;
    String holderLastName;

    public void setQRCode(String qrCode) {
        this.QRCode = qrCode;
    }

    public String toJSON(EventDTO event) {
        var eventPart = event != null ?
            "\"eventName\": " + event.getName() + ",\n"
            + "\"eventStartDate\": " + event.getStartDate().toString() + ",\n"
            + "\"eventEndDate\": " + event.getEndDate().toString() :
            "";

        return "{"
            + "\"id\": " + this.id + ",\n"
            + "\"ticketTypeId\": " + this.ticketType.getId() + ",\n"
            + "\"holderId\": " + this.getHolder().getId() + ",\n"
            + "\"purchaseDate\": " + this.purchaseDate + ",\n"
            + "\"QRCode\": " + this.QRCode + ",\n"
            + "\"holderFirstName\": " + this.holderFirstName + ",\n"
            + "\"holderLastName\": " + this.holderLastName + ",\n"
            + "\"eventId\": " + this.getEvent().getId() + ",\n"
            + eventPart
        + "}";
    }
}
