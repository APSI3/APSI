package apsi.team3.backend.repository;

import apsi.team3.backend.model.EventImage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventImageRepository extends JpaRepository<EventImage, Long> {
    @Query(value = "SELECT * FROM event_images WHERE event_id=?", nativeQuery = true)
    List<EventImage> findByEventId(Long eventId);
}
