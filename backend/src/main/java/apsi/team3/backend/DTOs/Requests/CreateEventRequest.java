package apsi.team3.backend.DTOs.Requests;

import java.util.Date;

public class CreateEventRequest {
    public String name;
    public Date startDate;
    public Date endDate;
    public String description;

    public CreateEventRequest(String name, Date startDate, Date endDate, String description) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }
}
