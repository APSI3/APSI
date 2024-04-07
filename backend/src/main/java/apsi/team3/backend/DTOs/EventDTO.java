package apsi.team3.backend.DTOs;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;
import java.time.LocalDateTime;

@Relation(collectionRelation = "events", itemRelation = "event")
@EqualsAndHashCode(callSuper = false)
@Value
public class EventDTO extends RepresentationModel<EventDTO> implements Serializable {
    Long id;
    String name;
    LocalDateTime startDate;
    LocalDateTime endDate;
    String description;
    Long organizerId;
}
