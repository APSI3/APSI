package apsi.team3.backend.interfaces;

import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.DTOs.Requests.CreateTicketRequest;
import apsi.team3.backend.exceptions.ApsiValidationException;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.Optional;

import com.google.zxing.WriterException;

public interface ITicketService {
    Optional<TicketDTO> getTicketById(Long id);
    TicketDTO create(CreateTicketRequest request) throws ApsiValidationException, MessagingException, WriterException, IOException;
}
