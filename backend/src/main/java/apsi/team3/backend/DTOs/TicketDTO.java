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
    Long sectionId;
    String holderFirstName;
    String holderLastName;

    public void setQRCode(String qrCode) {
        this.QRCode = qrCode;
    }

    public String toJSON() {
        var event = this.event;
        var eventPart = event != null ?
            "\"eventName\": " + event.getName() + ",\n"
            + "\"eventStartDate\": " + event.getStartDate().toString() + ",\n"
            + "\"eventEndDate\": " + event.getEndDate().toString() :
            "";

        var locationId = this.event != null ? this.event.getLocation() != null ? this.event.getLocation().getId() : null : null;
        var locationPart = locationId != null ? "\"locationId\": " + locationId + ",\n" : "";

        return "{"
            + "\"id\": " + this.id + ",\n"
            + "\"ticketTypeId\": " + this.ticketType.getId() + ",\n"
            + "\"holderId\": " + this.getHolder().getId() + ",\n"
            + "\"purchaseDate\": " + this.purchaseDate + ",\n"
            + "\"sectionId\": " + this.sectionId + ",\n"
            + "\"holderFirstName\": " + this.holderFirstName + ",\n"
            + "\"holderLastName\": " + this.holderLastName + ",\n"
            + "\"eventId\": " + this.getEvent().getId() + ",\n"
            + "\"eventId\": " + this.getEvent().getId() + ",\n"
            + locationPart
            + eventPart
        + "}";
    }
}
