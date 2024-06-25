package apsi.team3.backend.repository;

import apsi.team3.backend.model.Event;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "SELECT * FROM events e WHERE e.start_date <= :_to AND e.start_date >= :_from ORDER BY e.start_date",
        countQuery = "SELECT COUNT(*) FROM events e WHERE e.start_date <= :_to AND e.start_date >= :_from",
        nativeQuery = true)
    Page<Event> getEventsWithDatesBetween(Pageable pageable, @Param("_from") LocalDate from, @Param("_to") LocalDate to);

    @Query(
        value = "SELECT * FROM events e WHERE e.organizerId = :_userId AND e.start_date <= :_to AND e.start_date >= :_from ORDER BY e.start_date",
        countQuery = "SELECT COUNT(*) FROM events e WHERE e.organizerId = :_userId AND e.start_date <= :_to AND e.start_date >= :_from",
        nativeQuery = true)
    Page<Event> getEventsWithDatesBetweenForOrganizer(Pageable pageable, @Param("_from") LocalDate from, @Param("_to") LocalDate to, @Param("_userId") Long userId);

}
