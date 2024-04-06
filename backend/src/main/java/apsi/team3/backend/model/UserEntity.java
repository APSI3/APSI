package apsi.team3.backend.model;

import jakarta.persistence.*;

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

    public UserEntity() {}

    public UserEntity(Long id, String login) {
        this.id = id;
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public UserTypes getType(){
        return type;
    }

    public void setType(UserTypes type) {
        this.type = type;
    }
}
