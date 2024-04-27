package apsi.team3.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apsi.team3.backend.DTOs.CountryDTO;
import apsi.team3.backend.interfaces.ICountryService;

@RestController
@RequestMapping("/countries")
@CrossOrigin(origins = { "http://localhost:3000" }, allowCredentials = "true")
public class CountryController {
    private final ICountryService countryService;

    @Autowired
    public CountryController(ICountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public ResponseEntity<List<CountryDTO>> getAllCountries() {
        var allCountries = countryService.getAllCountries();
        return ResponseEntity.ok(allCountries);
    }
}
