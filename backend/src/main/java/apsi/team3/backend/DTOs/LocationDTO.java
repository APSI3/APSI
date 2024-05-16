package apsi.team3.backend.DTOs;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LocationDTO implements Serializable {
    Long id;
    Long country_id;
    int capacity;
    String description;
    String city;
    String street;
    String building_nr;
    String apartment_nr;
    String zip_code;
    Long creator_id;
}
