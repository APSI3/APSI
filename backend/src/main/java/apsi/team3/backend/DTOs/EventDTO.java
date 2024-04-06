package apsi.team3.backend.DTOs;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
public class EventDTO implements Serializable {
    Long id;
    String name;
    LocalDateTime startDate;
    LocalDateTime endDate;
    String description;
    Long organizerId;
}
