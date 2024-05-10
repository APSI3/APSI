package apsi.team3.backend.DTOs;

import lombok.EqualsAndHashCode;
import lombok.Value;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Relation(collectionRelation = "events", itemRelation = "event")
@EqualsAndHashCode(callSuper = false)
@Value
public class EventDTO extends RepresentationModel<EventDTO> implements Serializable {
    Long id;
    String name;
    LocalDate startDate;
    LocalTime startTime;
    LocalDate endDate;
    LocalTime endTime;
    String description;
    Long organizerId;
    LocationDTO location;
    List<TicketTypeDTO> ticketTypes;
}
