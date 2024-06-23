package apsi.team3.backend.repository;

import apsi.team3.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByLogin(String login);

    @Query(value = "SELECT COUNT(id) FROM users u WHERE u.login=:login", nativeQuery = true)
    int getUserLoginCount(@Param("login") String login);
}
