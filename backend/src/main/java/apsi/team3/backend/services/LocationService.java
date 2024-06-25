package apsi.team3.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import apsi.team3.backend.DTOs.PaginatedList;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.model.User;
import apsi.team3.backend.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.LocationDTO;
import apsi.team3.backend.interfaces.ILocationService;
import apsi.team3.backend.repository.LocationRepository;

@Service
public class LocationService implements ILocationService {
    private final int PAGE_SIZE = 10;
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
        if (loggedUser.getType().equals(UserType.SUPERADMIN)) {
            return StreamSupport.stream(locationRepository.findAll().spliterator(), false).map(DTOMapper::toDTO).toList();
        }
        return locationRepository.geLocationsForCreatorId(loggedUser.getId())
                .stream().map(DTOMapper::toDTO).toList();
    }

    @Override
    public PaginatedList<LocationDTO> getLocationsPageable(int pageIndex) throws ApsiValidationException {
        if (pageIndex < 0)
            throw new ApsiValidationException("Indeks strony nie może być ujemny", "pageIndex");
        var loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var page = loggedUser.getType().equals(UserType.SUPERADMIN)
                ? locationRepository.geLocationsPageable(PageRequest.of(pageIndex, PAGE_SIZE))
                : locationRepository.geLocationsForCreatorIdPageable(PageRequest.of(pageIndex, PAGE_SIZE), loggedUser.getId());

        var items = page
                .stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());

        return new PaginatedList<>(items, pageIndex, page.getTotalElements(), page.getTotalPages());
    }
}
