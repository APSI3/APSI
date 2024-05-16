package apsi.team3.backend.repository;

import apsi.team3.backend.model.EventImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventImageRepository extends JpaRepository<EventImage, Long> {}
