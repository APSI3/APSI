package apsi.team3.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ticket_types")
public class TicketType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "price", columnDefinition = "NUMERIC(10, 2)")
    private BigDecimal price;

    @Column(name = "quantity_available")
    private int quantityAvailable;
}
