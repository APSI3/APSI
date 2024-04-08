package apsi.team3.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "login", nullable = false, length = 128)
    private String login;

    @Column(name = "hash", nullable = false, length = 64)
    private String hash;

    @Column(name = "salt", nullable = false, length = 32)
    private String salt;

    @Column(name = "type", nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private UserType type;

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    private List<Event> events;

}
