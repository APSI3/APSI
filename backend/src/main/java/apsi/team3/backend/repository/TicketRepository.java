package apsi.team3.backend.repository;

import org.springframework.data.domain.Page;
import apsi.team3.backend.model.Ticket;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query(value = "SELECT t.id as id, ticket_type_id, holder_id, holder_first_name, holder_last_name, purchase_date, e.id as eventId, e.name as eventName, e.start_date as eventStartDate, e.start_time as eventStartTime, e.end_date as eventEndDate, e.end_time as eventEndTime, tt.name as ticketTypeName, price " +
            "FROM tickets t " +
            "LEFT JOIN ticket_types tt ON (t.ticket_type_id=tt.id) " +
            "LEFT JOIN events e ON (tt.event_id=e.id) " +
            "WHERE e.end_date <= :_to AND e.start_date >= :_from AND holder_id = :_holder_id ORDER BY e.start_date",
            countQuery = "SELECT COUNT(*) FROM tickets t LEFT JOIN ticket_types tt ON (t.ticket_type_id=tt.id) LEFT JOIN events e ON (tt.event_id=e.id) WHERE e.end_date <= :_to AND e.start_date >= :_from AND holder_id = :_holder_id ",
            nativeQuery = true)
    Page<Ticket> getUsersTicketsWithDatesBetween(Pageable pageable, @Param("_holder_id") Long holderId, @Param("_from") LocalDate from, @Param("_to") LocalDate to);
}
