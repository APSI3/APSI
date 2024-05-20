package apsi.team3.backend.interfaces;

import apsi.team3.backend.DTOs.LocationDTO;
import java.util.Optional;
import java.util.List;

public interface ILocationService {
    Optional<LocationDTO> getLocationById(Long id);
    LocationDTO create(LocationDTO locationDTO);
    List<LocationDTO> getLocations();
}
