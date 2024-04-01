package apsi.team3.backend.DTOs.Responses;

import apsi.team3.backend.DTOs.EventDTO;

public class CreateEventResponse {
    public EventDTO event;

    public CreateEventResponse(EventDTO event) {
        this.event = event;
    }
}
