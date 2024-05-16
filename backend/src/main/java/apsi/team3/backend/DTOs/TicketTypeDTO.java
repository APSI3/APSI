package apsi.team3.backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;
import java.math.BigDecimal;

@Relation(collectionRelation = "ticket_types", itemRelation = "ticket_types")
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TicketTypeDTO extends RepresentationModel<TicketTypeDTO> implements Serializable {
    Long id;
    Long eventId;
    String name;
    BigDecimal price;
    int quantityAvailable;
}
