package apsi.team3.backend.interfaces;

import apsi.team3.backend.DTOs.EventDTO;
import apsi.team3.backend.DTOs.ImageDTO;
import apsi.team3.backend.DTOs.PaginatedList;
import apsi.team3.backend.exceptions.ApsiValidationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface IEventService {
    Optional<EventDTO> getEventById(Long id);

    PaginatedList<EventDTO> getEvents(LocalDate from, LocalDate to, int pageIndex) throws ApsiValidationException;

    EventDTO create(EventDTO event, MultipartFile image, MultipartFile sectionMap) throws ApsiValidationException;

    EventDTO replace(EventDTO event, MultipartFile image, MultipartFile sectionMap) throws ApsiValidationException;

    void delete(Long id);

    boolean notExists(Long id);

    List<ImageDTO> getImagesByEventId(Long id);
}
