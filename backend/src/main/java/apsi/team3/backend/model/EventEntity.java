package apsi.team3.backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

import apsi.team3.backend.DTOs.Requests.CreateEventRequest;

@Entity
@Table(name = "events")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "description")
    private String description;

    @Column(name = "organizer_id")
    private Long organizerId;

    public EventEntity() {}

    public EventEntity(Long id, String name, LocalDateTime startDate, LocalDateTime endDate, String description, Long organizerId) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.organizerId = organizerId;
    }

    public EventEntity(CreateEventRequest request, Long userId) {
        this.name = request.name;
        this.startDate = request.startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.endDate = request.endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.description = request.description;
        this.organizerId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }
}
