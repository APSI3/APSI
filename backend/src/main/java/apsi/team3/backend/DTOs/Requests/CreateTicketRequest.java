package apsi.team3.backend.DTOs.Requests;

import lombok.Value;

@Value
public class CreateTicketRequest {
    long sectionId;
    long ticketTypeId;
    String holderFirstName;
    String holderLastName;
}
