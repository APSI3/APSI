package apsi.team3.backend.DTOs;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;
import java.math.BigDecimal;

@Relation(collectionRelation = "ticket_types", itemRelation = "ticket_type")
@EqualsAndHashCode(callSuper = false)
@Value
public class TicketTypeDTO extends RepresentationModel<TicketTypeDTO> implements Serializable {
    Long id;
    Long eventId;
    String name;
    BigDecimal price;
    int quantityAvailable;
}
