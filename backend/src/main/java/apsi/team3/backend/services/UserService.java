package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.LoggedUserDTO;
import apsi.team3.backend.DTOs.Requests.LoginRequest;
import apsi.team3.backend.exceptions.ApsiException;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IUserService;
import apsi.team3.backend.model.User;
import apsi.team3.backend.repository.UserRepository;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }

    @Override
    public String hashPassword(String password, String salt) throws ApsiException {
        try {
            var byteSalt = Hex.decodeHex(salt);
            var spec = new PBEKeySpec(password.toCharArray(), byteSalt, 2 ^ 12, 256);
            var factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            return Hex.encodeHexString(factory.generateSecret(spec).getEncoded());
        } catch (DecoderException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ApsiException(e);
        }
    }

    @Override
    public LoggedUserDTO login(LoginRequest request) throws ApsiValidationException {
        if (request.getLogin() == null || request.getLogin().isBlank())
            throw new ApsiValidationException("Login jest wymagany", "login");
        if (request.getPassword() == null || request.getPassword().isBlank())
            throw new ApsiValidationException("Hasło jest wymagane", "password");

        var userRaw = userRepository.findUserByLogin(request.getLogin());
        var user = userRaw.orElseThrow(() -> new ApsiValidationException("Niepoprawne hasło", "password"));

        try {
            var hash = hashPassword(request.getPassword(), user.getSalt());

            if (Objects.equals(hash, user.getHash())) {
                var str = user.getLogin() + ":" + request.getPassword();
                var encoded = Base64.getEncoder().encodeToString(str.getBytes());
                var header = "Basic " + encoded;
                return DTOMapper.toDTO(user, header);
            }
        } catch (ApsiException e) {
            throw new ApsiValidationException("Nie udało się zalogować", "password");
        }

        throw new ApsiValidationException("Niepoprawne hasło", "password");
    }
}
