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

    @Column(name = "email", nullable = true, length = 255)
    private String email;

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    private List<Event> events;

    public User(String login, String hash, String salt, UserType type, String email) {
        this.login = login;
        this.hash = hash;
        this.salt = salt;
        this.type = type;
        this.email = email;
    }
}
