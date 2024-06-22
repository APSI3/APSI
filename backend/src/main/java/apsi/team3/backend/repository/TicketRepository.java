package apsi.team3.backend.repository;

import apsi.team3.backend.model.SectionCountResult;
import apsi.team3.backend.model.Ticket;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT new apsi.team3.backend.model.SectionCountResult(section.id section_id, COUNT(*) count) FROM ticket WHERE ticketType.event.Id = :eventId GROUP BY section.id")
    public List<SectionCountResult> countTicketsBySectionForEvent(@Param("eventId") long eventId);

    @Query("SELECT COUNT(*) FROM ticket WHERE section.id = :sectionId")
    public Optional<Long> countTicketsForSectionId(@Param("sectionId") long sectionId);
}
