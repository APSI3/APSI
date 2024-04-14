package apsi.team3.backend.repository;

import apsi.team3.backend.model.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
    @Query(value = "SELECT * FROM ticket_types WHERE event_id=?", nativeQuery = true)
    List<TicketType> findByEventId(Long eventId);
}
