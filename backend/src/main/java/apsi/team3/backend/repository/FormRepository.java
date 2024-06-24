package apsi.team3.backend.repository;

import apsi.team3.backend.model.Form;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormRepository extends JpaRepository<Form, Long> {
    @Query(value = "SELECT * FROM forms", nativeQuery = true)
    Page<Form> getForms(Pageable pageable);
}
