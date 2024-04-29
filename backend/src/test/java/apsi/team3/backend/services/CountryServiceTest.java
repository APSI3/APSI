package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.CountryDTO;
import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.model.Country;
import apsi.team3.backend.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
public class CountryServiceTest {
    @Mock
    CountryRepository countryRepository;

    @InjectMocks
    CountryService countryService;

    @Test
    public void testGetAllCountries() {
        List<Country> countries = new ArrayList<>();
        countries.add(new Country(1L, "PL", "POLSKA")); //gurom
        countries.add(new Country(2L, "AF", "Afganistan"));
        when(countryRepository.findAll()).thenReturn(countries);
        List<CountryDTO> countryDTOS = new ArrayList<>();
        for (var country: countries) {
            countryDTOS.add(DTOMapper.toDTO(country));
        }
        assertEquals(countryService.getAllCountries(), countryDTOS);
    }
}
