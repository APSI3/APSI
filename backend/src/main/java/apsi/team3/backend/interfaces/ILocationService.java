package apsi.team3.backend.interfaces;

import apsi.team3.backend.DTOs.LocationDTO;
import java.util.Optional;

public interface ILocationService {
    Optional<LocationDTO> getLocationById(Long id);
    LocationDTO create(LocationDTO locationDTO);
}
