package apsi.team3.backend.DTOs;

import lombok.Value;

@Value
public class CountryDTO {
    Long id;
    String code;
    String full_name;
}
