package apsi.team3.backend.DTOs;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationDTO that = (LocationDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(country_id, that.country_id) &&
            Objects.equals(capacity, that.capacity) &&
            Objects.equals(description, that.description) &&
            Objects.equals(city, that.city) &&
            Objects.equals(street, that.street) &&
            Objects.equals(building_nr, that.building_nr) &&
            Objects.equals(apartment_nr, that.apartment_nr) &&
            Objects.equals(zip_code, that.zip_code) &&
            Objects.equals(creator_id, that.creator_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, country_id, capacity, description, city, street, building_nr, apartment_nr, zip_code, creator_id);
    }
}
