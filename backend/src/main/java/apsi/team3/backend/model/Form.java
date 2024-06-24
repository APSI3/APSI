package apsi.team3.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "forms")
public class Form {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "login", nullable = false, length = 128)
    private String login;

    @Column(name = "salt", nullable = false, length = 32)
    private String salt;

    @Column(name = "hash", nullable = false, length = 64)
    private String hash;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "email", nullable = true, length = 255)
    private String email;

    public Form(String login, String salt, String hash, String status, String email) {
        this.login = login;
        this.salt = salt;
        this.hash = hash;
        this.status = status;
        this.email = email;
    }
}
