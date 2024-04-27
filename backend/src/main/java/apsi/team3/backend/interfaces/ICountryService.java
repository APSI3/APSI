package apsi.team3.backend.interfaces;

import java.util.List;

import apsi.team3.backend.DTOs.CountryDTO;

public interface ICountryService {
    List<CountryDTO> getAllCountries();
}
