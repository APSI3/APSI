package apsi.team3.backend.services;

import apsi.team3.backend.exceptions.ApsiException;
import apsi.team3.backend.interfaces.IUserService;
import apsi.team3.backend.model.UserEntity;
import apsi.team3.backend.repository.UserRepository;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;

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
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public String hashPassword(String password, String salt) throws ApsiException {
        try {
            var byteSalt = Hex.decodeHex(salt);
            var spec = new PBEKeySpec(password.toCharArray(), byteSalt, 2^12, 128);
            var factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            
            return Hex.encodeHexString(factory.generateSecret(spec).getEncoded());
        }
        catch (DecoderException|NoSuchAlgorithmException|InvalidKeySpecException e) {
            throw new ApsiException(e);
        }
    }
}
