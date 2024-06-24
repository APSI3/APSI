package apsi.team3.backend.DTOs;

import java.util.List;

import lombok.Value;

@Value
public class EventReportDTO {
    EventDTO event;
    List<TicketDTO> tickets;
}
