package apsi.team3.backend.repository;

import apsi.team3.backend.model.Location;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {
    @Query(value = "SELECT * FROM locations WHERE creator_id=?", nativeQuery = true)
    public List<Location> geLocationsForCreatorId(Long creatorId);
}
