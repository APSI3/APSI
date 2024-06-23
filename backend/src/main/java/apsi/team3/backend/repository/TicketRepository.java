package apsi.team3.backend.repository;

import apsi.team3.backend.model.SectionCountResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import apsi.team3.backend.model.Ticket;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT new apsi.team3.backend.model.SectionCountResult(section.id section_id, COUNT(*) count) FROM ticket WHERE ticketType.event.Id = :eventId GROUP BY section.id")
    public List<SectionCountResult> countTicketsBySectionForEvent(@Param("eventId") long eventId);

    @Query("SELECT COUNT(*) FROM ticket WHERE section.id = :sectionId")
    public Optional<Long> countTicketsForSectionId(@Param("sectionId") long sectionId);

    @Query(value = "SELECT t.id as id, ticket_type_id, holder_id, holder_first_name, holder_last_name, purchase_date, e.id as eventId, e.name as eventName, e.start_date as eventStartDate, e.start_time as eventStartTime, e.end_date as eventEndDate, e.end_time as eventEndTime, tt.name as ticketTypeName, price, section_id " +
            "FROM tickets t " +
            "LEFT JOIN ticket_types tt ON (t.ticket_type_id=tt.id) " +
            "LEFT JOIN events e ON (tt.event_id=e.id) " +
            "WHERE e.start_date <= :_to AND e.start_date >= :_from AND holder_id = :_holder_id ORDER BY e.start_date",
            countQuery = "SELECT COUNT(*) FROM tickets t LEFT JOIN ticket_types tt ON (t.ticket_type_id=tt.id) LEFT JOIN events e ON (tt.event_id=e.id) WHERE e.start_date <= :_to AND e.start_date >= :_from AND holder_id = :_holder_id ",
            nativeQuery = true)
    Page<Ticket> getUsersTicketsWithDatesBetween(Pageable pageable, @Param("_holder_id") Long holderId, @Param("_from") LocalDate from, @Param("_to") LocalDate to);

    @Query(value = "SELECT t.id as id, ticket_type_id, holder_id, holder_first_name, holder_last_name, purchase_date, tt.name as ticketTypeName, price FROM tickets t LEFT JOIN ticket_types tt ON (t.ticket_type_id=tt.id) WHERE event_id = :eventId", nativeQuery = true)
    Ticket[] getTicketsByEventId(Long eventId);

    @Query(value = "SELECT t.id as id, ticket_type_id, holder_id, holder_first_name, holder_last_name, purchase_date, tt.name as ticketTypeName, price FROM tickets t LEFT JOIN ticket_types tt ON (t.ticket_type_id=tt.id) WHERE ticket_type_id = :ticketTypeId", nativeQuery = true)
    Ticket[] getByTicketTypeId(Long ticketTypeId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM tickets WHERE ticket_type_id = :ticketTypeId", nativeQuery = true)
    int deleteByTicketTypeId(Long ticketTypeId);
}
