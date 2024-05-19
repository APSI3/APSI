package apsi.team3.backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = false)
@Getter
@AllArgsConstructor
@Setter
public class TicketDTO extends RepresentationModel<TicketDTO> {
    Long id;
    Long ticketTypeId;
    Long holderId;
    Long eventId;
    LocalDate purchaseDate;
    String QRCode;
    String holderFirstName;
    String holderLastName;

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
