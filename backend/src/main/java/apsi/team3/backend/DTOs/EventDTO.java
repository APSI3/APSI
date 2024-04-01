package apsi.team3.backend.DTOs;

import java.time.LocalDateTime;

public class EventDTO {
    public long id;
    public String name;
    public LocalDateTime startDate;
    public LocalDateTime endDate;
    public String description;
    public Long organizerId;
    
    public EventDTO(long id, String name, LocalDateTime startDate, LocalDateTime endDate, String description,
            Long organizerId) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.organizerId = organizerId;
    }
}
