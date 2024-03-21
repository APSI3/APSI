package apsi.team3.backend.interfaces;

import apsi.team3.backend.model.UserEntity;

import java.util.List;

public interface IUserService {
    UserEntity getUserById(Long id);
    List<UserEntity> getAllUsers();
}
