package apsi.team3.backend.repository;

import apsi.team3.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByLogin(String login);

    @Query(value = "SELECT * FROM users WHERE canceled = FALSE", countQuery = "SELECT COUNT(*) FROM users", nativeQuery = true)
    Page<User> getUsers(Pageable pageable);

    @Query(value = "SELECT COUNT(id) FROM users u WHERE u.login=:login", nativeQuery = true)
    int getUserLoginCount(@Param("login") String login);
}
