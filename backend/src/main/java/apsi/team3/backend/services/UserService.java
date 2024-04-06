package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.LoggedUserDTO;
import apsi.team3.backend.DTOs.Requests.LoginRequest;
import apsi.team3.backend.DTOs.Responses.LoginResponse;
import apsi.team3.backend.exceptions.ApsiException;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IUserService;
import apsi.team3.backend.model.UserEntity;
import apsi.team3.backend.repository.UserRepository;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;
import java.util.Objects;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserEntity> getUserById(Long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public Optional<UserEntity> getUserByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }

    @Override
    public String hashPassword(String password, String salt) throws ApsiException {
        try {
            var byteSalt = Hex.decodeHex(salt);
            var spec = new PBEKeySpec(password.toCharArray(), byteSalt, 2^12, 256);
            var factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            
            return Hex.encodeHexString(factory.generateSecret(spec).getEncoded());
        }
        catch (DecoderException|NoSuchAlgorithmException|InvalidKeySpecException e) {
            throw new ApsiException(e);
        }
    }

    @Override
    public LoginResponse login(LoginRequest request) throws ApsiValidationException {
        if (request.login == null || request.login.isBlank())
            throw new ApsiValidationException("Login jest wymagany", "login");
        if (request.password == null || request.password.isBlank())
            throw new ApsiValidationException("Hasło jest wymagane", "password");
        
        var userRaw = userRepository.findUserByLogin(request.login);
        if (userRaw.isEmpty())
            throw new ApsiValidationException("Niepoprawne hasło", "password");
        
        var user = userRaw.get();
        try {
            var hash = hashPassword(request.password, user.getSalt());

            if (Objects.equals(hash, user.getHash())){
                var str = user.getLogin() + ":" + request.password;
                var encoded = Base64.getEncoder().encodeToString(str.getBytes()); 
                var header = "Basic " + encoded;
                return new LoginResponse(new LoggedUserDTO(user.getId(), user.getLogin(), header, user.getType()));
            }
        }
        catch (ApsiException e){
            throw new ApsiValidationException("Nie udało się zalogować", "password");
        }

        throw new ApsiValidationException("Niepoprawne hasło", "password");
    }
}
