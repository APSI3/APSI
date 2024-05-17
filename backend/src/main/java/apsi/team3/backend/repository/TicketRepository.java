package apsi.team3.backend.repository;

import apsi.team3.backend.DTOs.PaginatedList;
import apsi.team3.backend.DTOs.TicketDTO;
import apsi.team3.backend.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query(value = "SELECT * FROM tickets WHERE holder_id=?", nativeQuery = true)
    List<Ticket> findByHolderId(Long holderId);
}
