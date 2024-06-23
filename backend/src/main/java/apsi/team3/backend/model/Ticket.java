package apsi.team3.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "ticket")
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "holder_id", nullable = false)
    private User holder;

    @ManyToOne(optional = false)
    @JoinColumn(name = "section_id", nullable = false)
    private EventSection section;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ticket_type_id", nullable = false)
    private TicketType ticketType;

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @Column(name = "holder_first_name", nullable = false)
    private String holderFirstName;

    @Column(name = "holder_last_name", nullable = false)
    private String holderLastName;
}
