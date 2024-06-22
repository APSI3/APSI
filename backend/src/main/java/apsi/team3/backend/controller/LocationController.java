package apsi.team3.backend.controller;

import java.util.Optional;
import java.util.List;

import apsi.team3.backend.DTOs.PaginatedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import apsi.team3.backend.DTOs.LocationDTO;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.ILocationService;

@RestController
@RequestMapping("/locations")
@CrossOrigin(origins = { "http://localhost:3000" }, allowCredentials = "true")
public class LocationController {
    private final ILocationService locationService;

    @Autowired
    public LocationController(ILocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable("id") Long id) {
        Optional<LocationDTO> ticket = locationService.getLocationById(id);
        return ticket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@RequestBody LocationDTO locationDTO) throws ApsiValidationException {
        var resp = locationService.create(locationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping
    public ResponseEntity<List<LocationDTO>> getLocations(){
        var resp = locationService.getLocations();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/pageable")
    public ResponseEntity<PaginatedList<LocationDTO>> getLocationsPageable(
            @RequestParam int pageIndex
    ) throws ApsiValidationException {
        var resp = locationService.getLocationsPageable(pageIndex);
        return ResponseEntity.ok(resp);
    }
}
