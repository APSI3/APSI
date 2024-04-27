package apsi.team3.backend.DTOs;

import java.io.Serializable;
import lombok.Value;

@Value
public class LocationDTO implements Serializable {
    Long id;
    Long country_id;
    int capacity;
    String city;
    String street;
    String building_nr;
    String apartment_nr;
    String zip_code;
    Long creator_id;
}
