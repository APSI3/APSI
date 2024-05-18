package apsi.team3.backend.DTOs;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@Relation(collectionRelation = "tickets", itemRelation = "tickets")
@EqualsAndHashCode(callSuper = false)
public class TicketDTO extends RepresentationModel<TicketDTO> {
    @Getter
    Long id;
    @Getter
    Long ticketTypeId;
    @Getter
    Long holderId;
    @Getter
    LocalDate purchaseDate;
    @Getter
    String QRCode;

    public TicketDTO(Long id, Long ticketTypeId, Long holderId, LocalDate purchaseDate) {
        this.id = id;
        this.ticketTypeId = ticketTypeId;
        this.holderId = holderId;
        this.purchaseDate = purchaseDate;
    }

    public void setQRCode(String qrCode) {
        this.QRCode = qrCode;
    }

    @SuppressWarnings("null")
    @Override
    public String toString() {
        return "{\"id\": " + this.id + ",\n"
                + "\"ticketTypeId\": " + this.ticketTypeId + ",\n"
                + "\"holderId\": " + this.holderId + ",\n"
                + "\"purchaseDate\": " + this.purchaseDate + ",\n"
                + "\"QRCode\": " + this.QRCode + "}";
    }
}
