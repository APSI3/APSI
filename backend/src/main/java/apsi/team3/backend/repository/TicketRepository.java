package apsi.team3.backend.repository;

import apsi.team3.backend.model.SectionCountResult;
import apsi.team3.backend.model.Ticket;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT section_id, COUNT(*) FROM tickets WHERE eventId = ? GROUP BY section_id")
    public List<SectionCountResult> countTicketsBySectionForEvent(long eventId);
}
