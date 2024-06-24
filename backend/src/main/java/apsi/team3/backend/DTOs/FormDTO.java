package apsi.team3.backend.DTOs;

import lombok.*;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class FormDTO {
    private Long id;
    private String login;
    private String email;
    private String salt;
    private String status;
}
