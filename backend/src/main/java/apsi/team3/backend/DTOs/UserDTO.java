package apsi.team3.backend.DTOs;

import lombok.*;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class UserDTO implements Serializable {
    private Long id;
    private String login;

}
