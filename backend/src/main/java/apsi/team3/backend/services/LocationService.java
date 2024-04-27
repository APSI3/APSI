package apsi.team3.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.LocationDTO;
import apsi.team3.backend.interfaces.ILocationService;
import apsi.team3.backend.repository.LocationRepository;

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
        var saved = locationRepository.save(loc);
        return DTOMapper.toDTO(saved);
    }
}
