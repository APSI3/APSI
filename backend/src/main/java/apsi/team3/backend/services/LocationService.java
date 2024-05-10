package apsi.team3.backend.services;

import java.util.List;
import java.util.Optional;

import apsi.team3.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.LocationDTO;
import apsi.team3.backend.interfaces.ILocationService;
import apsi.team3.backend.repository.LocationRepository;

@Service
public class LocationService implements ILocationService {
    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) { this.locationRepository = locationRepository; }

    @Override
    public Optional<LocationDTO> getLocationById(Long id) {
        return locationRepository.findById(id).map(DTOMapper::toDTO);
    }

    @Override
    public LocationDTO create(LocationDTO locationDTO) {
        var loc = DTOMapper.toEntity(locationDTO);
        var loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        loc.setCreator(loggedUser);
        var saved = locationRepository.save(loc);
        return DTOMapper.toDTO(saved);
    }

    @Override
    public List<LocationDTO> getLocations() {
        var loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return locationRepository.geLocationsForCreatorId(loggedUser.getId())
            .stream().map(DTOMapper::toDTO).toList();
    }
}
