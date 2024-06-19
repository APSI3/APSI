package apsi.team3.backend.repository;

import apsi.team3.backend.model.EventSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventSectionRepository extends JpaRepository<EventSection, Long> {}
