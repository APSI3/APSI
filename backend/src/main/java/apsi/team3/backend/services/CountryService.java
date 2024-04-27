package apsi.team3.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apsi.team3.backend.DTOs.CountryDTO;
import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.interfaces.ICountryService;
import apsi.team3.backend.repository.CountryRepository;

@Service
public class CountryService implements ICountryService {
    private final CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository) { this.countryRepository = countryRepository; }

    @Override 
    public List<CountryDTO> getAllCountries() {
        return countryRepository.findAll()
            .stream()
            .map(DTOMapper::toDTO)
            .collect(Collectors.toList());
    }
}
