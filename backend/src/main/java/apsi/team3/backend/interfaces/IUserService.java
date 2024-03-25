package apsi.team3.backend.interfaces;

import apsi.team3.backend.exceptions.ApsiException;
import apsi.team3.backend.model.UserEntity;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    Optional<UserEntity> getUserById(Long id);
    Optional<UserEntity> getUserByLogin(String login);
    List<UserEntity> getAllUsers();
    String hashPassword(String password, String salt) throws ApsiException;
}
