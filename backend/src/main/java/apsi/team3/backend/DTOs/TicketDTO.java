package apsi.team3.backend.DTOs;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@Relation(collectionRelation = "tickets", itemRelation = "tickets")
@EqualsAndHashCode(callSuper = false)
@Value
public class TicketDTO extends RepresentationModel {
    Long id;
    Long ticketTypeId;
    Long holderId;
    LocalDate purchaseDate;
}
