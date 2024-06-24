package apsi.team3.backend.DTOs;

import java.util.List;

import lombok.Value;

@Value
public class EventReportDTO {
    EventDTO eventDTO;
    List<TicketDTO> tickets;
}
