package apsi.team3.backend.interfaces;

import apsi.team3.backend.DTOs.PaginatedList;
import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.DTOs.Requests.CreateTicketRequest;
import apsi.team3.backend.exceptions.ApsiValidationException;

import jakarta.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.google.zxing.WriterException;

public interface ITicketService {
    Optional<TicketDTO> getTicketById(Long id);
    TicketDTO create(CreateTicketRequest request) throws ApsiValidationException, MessagingException, WriterException, IOException;
    PaginatedList<TicketDTO> getTicketsByUserId(Long id, LocalDate from, LocalDate to, int pageIndex) throws ApsiValidationException;
    List<TicketDTO> getTicketsByEventId(Long id) throws ApsiValidationException;
    List<TicketDTO> getTicketsByTicketTypeId(Long id) throws ApsiValidationException;
    void deleteByTicketTypeId(Long id);
    void deleteByHolderId(Long id);
}
