package apsi.team3.backend.DTOs.Responses;

import apsi.team3.backend.DTOs.EventDTO;

import java.util.List;

public class GetEventsResponse {
    public List<EventDTO> events;

    public GetEventsResponse(List<EventDTO> events) { this.events = events; }
}
