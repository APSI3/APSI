package apsi.team3.backend.interfaces;

import apsi.team3.backend.DTOs.LocationDTO;
import apsi.team3.backend.DTOs.PaginatedList;
import apsi.team3.backend.exceptions.ApsiValidationException;

import java.util.List;
import java.util.Optional;

public interface ILocationService {
    Optional<LocationDTO> getLocationById(Long id);
    LocationDTO create(LocationDTO locationDTO);
    PaginatedList<LocationDTO> getLocationsPageable(int pageIndex) throws ApsiValidationException;
    List<LocationDTO> getLocations();
}
