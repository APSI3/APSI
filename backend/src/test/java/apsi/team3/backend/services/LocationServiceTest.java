package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.LocationDTO;
import apsi.team3.backend.model.Country;
import apsi.team3.backend.model.Location;
import apsi.team3.backend.model.User;
import apsi.team3.backend.model.UserType;
import apsi.team3.backend.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
public class LocationServiceTest {

    @Mock
    LocationRepository locationRepository;

    @InjectMocks
    LocationService locationService;

    @Test
    public void testGetLocationById() {
        User creator = new User(1L, "login", "hash", "salt", UserType.ORGANIZER, new ArrayList<>());
        Location location = new Location(
                1L,
                creator,
                new Country(1L, "PL", "POLSKA"),
                100,
                "Warszawa",
                "Waryńskiego",
                "12",
                "1601",
                "00-631"
        );
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        assertEquals(locationService.getLocationById(1L), Optional.of(DTOMapper.toDTO(location)));
    }

    @Test
    public void testCreate() {
        User creator = new User(1L, "login", "hash", "salt", UserType.ORGANIZER, new ArrayList<>());
        Location location = new Location(
                null,
                creator,
                new Country(1L, "PL", "POLSKA"),
                100,
                "Warszawa",
                "Waryńskiego",
                "12",
                "1601",
                "00-631"
        );
        LocationDTO locationDTO = DTOMapper.toDTO(location);
        location.setId(1L);
        LocationDTO expectedDTO = DTOMapper.toDTO(location);
        when(locationRepository.save(any())).thenReturn(location);
        assertEquals(locationService.create(locationDTO), expectedDTO);
    }
}
