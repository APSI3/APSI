package apsi.team3.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "HASH", nullable = false)
    private String hash;

    @Column(name = "SALT", nullable = false)
    private String salt;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserTypes type;

}
