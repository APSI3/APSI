package apsi.team3.backend.DTOs;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Relation(collectionRelation = "tickets", itemRelation = "tickets")
@EqualsAndHashCode(callSuper = false)
@Getter
public class ExtendedTicketDTO extends TicketDTO {
    Long eventId;
    String eventName;
    LocalDate eventStartDate;
    LocalTime eventStartTime;
    LocalDate eventEndDate;
    LocalTime eventEndTime;
    String ticketTypeName;
    BigDecimal price;

    public ExtendedTicketDTO(
            Long id,
            Long ticketTypeId,
            Long holderId,
            LocalDate purchaseDate,
            Long eventId,
            String eventName,
            LocalDate eventStartDate,
            LocalTime eventStartTime,
            LocalDate eventEndDate,
            LocalTime eventEndTime,
            String ticketTypeName,
            BigDecimal price
    ) {
        super(id, ticketTypeId, holderId, purchaseDate);
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventStartDate = eventStartDate;
        this.eventStartTime = eventStartTime;
        this.eventEndDate = eventEndDate;
        this.eventEndTime = eventEndTime;
        this.ticketTypeName = ticketTypeName;
        this.price = price;
    }

    @SuppressWarnings("null")
    @Override
    public String toString() {
        return "{\"id\": " + this.id + ",\n"
                + "\"ticketTypeId\": " + this.ticketTypeId + ",\n"
                + "\"holderId\": " + this.holderId + ",\n"
                + "\"purchaseDate\": " + this.purchaseDate + ",\n"
                + "\"eventId\": " + this.eventId + ",\n"
                + "\"eventName\": " + this.eventName + ",\n"
                + "\"eventStartDate\": " + this.eventStartDate + ",\n"
                + "\"eventStartTime\": " + this.eventStartTime + ",\n"
                + "\"eventEndDate\": " + this.eventEndDate + ",\n"
                + "\"eventEndTime\": " + this.eventEndTime + ",\n"
                + "\"ticketTypeName\": " + this.ticketTypeName + ",\n"
                + "\"price\": " + this.price + ",\n"
                + "\"QRCode\": " + this.QRCode + "}";
    }
}
