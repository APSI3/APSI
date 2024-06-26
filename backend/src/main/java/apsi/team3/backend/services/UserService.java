package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.*;
import apsi.team3.backend.DTOs.Requests.CreateUserRequest;
import apsi.team3.backend.DTOs.Requests.LoginRequest;
import apsi.team3.backend.exceptions.ApsiException;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IEventService;
import apsi.team3.backend.interfaces.ITicketService;
import apsi.team3.backend.interfaces.IUserService;
import apsi.team3.backend.model.Form;
import apsi.team3.backend.model.User;
import apsi.team3.backend.model.UserType;
import apsi.team3.backend.repository.UserRepository;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    private final int PAGE_SIZE = 10;
    private static int SALT_LENGTH = 16;
    private static final SecureRandom secureRandom = new SecureRandom();
    private final UserRepository userRepository;
    private final ITicketService ticketService;
    private final IEventService eventService;

    @Autowired
    public UserService(UserRepository userRepository, ITicketService ticketService, IEventService eventService) {
        this.userRepository = userRepository;
        this.ticketService = ticketService;
        this.eventService = eventService;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }

    static public String hashPassword(String password, String salt) throws ApsiException {
        try {
            var byteSalt = Hex.decodeHex(salt);
            var spec = new PBEKeySpec(password.toCharArray(), byteSalt, 2 ^ 12, 256);
            var factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            return Hex.encodeHexString(factory.generateSecret(spec).getEncoded());
        } catch (DecoderException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ApsiException(e);
        }
    }

    public static String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);
        return Hex.encodeHexString(salt);
    }

    @Override
    public LoggedUserDTO login(LoginRequest request) throws ApsiValidationException {
        if (request.getLogin() == null || request.getLogin().isBlank())
            throw new ApsiValidationException("Login jest wymagany", "login");
        if (request.getPassword() == null || request.getPassword().isBlank())
            throw new ApsiValidationException("Hasło jest wymagane", "password");

        var userRaw = userRepository.findUserByLogin(request.getLogin());
        var user = userRaw.orElseThrow(() -> new ApsiValidationException("Niepoprawny login lub hasło", "password"));

        String hash;
        try {
            hash = hashPassword(request.getPassword(), user.getSalt());
        } catch (ApsiException e) {
            throw new ApsiValidationException("Nie udało się zalogować", "password");
        }

        if (Objects.equals(hash, user.getHash())) {
            if (user.isCanceled()) {
                throw new ApsiValidationException("Użytkownik został usunięty", "login");
            }
            
            var str = user.getLogin() + ":" + request.getPassword();
            var encoded = Base64.getEncoder().encodeToString(str.getBytes());
            var header = "Basic " + encoded;
            return DTOMapper.toDTO(user, header);
        }

        throw new ApsiValidationException("Niepoprawny login lub hasło", "password");
    }

    @Override
    public UserDTO createUser(CreateUserRequest request) throws ApsiException {
        if (request.getLogin() == null || request.getLogin().isBlank())
            throw new ApsiValidationException("Login jest wymagany", "login");
        if (request.getPassword() == null || request.getPassword().isBlank())
            throw new ApsiValidationException("Hasło jest wymagane", "password");
        if (request.getEmail() == null || request.getEmail().isBlank())
            throw new ApsiValidationException("Email jest wymagany", "email");

        var loginCount = getUserLoginCount(request.getLogin());
        if (loginCount > 0)
            throw new ApsiValidationException("Login już zajęty", "login");

        var salt = generateSalt();
        var hash = hashPassword(request.getPassword(), salt);
        var entity = new User(request.getLogin(), hash, salt, UserType.PERSON, request.getEmail(), false);
        var newUser = userRepository.save(entity);
        return DTOMapper.toDTO(newUser);
    }

    @Override
    public int getUserLoginCount(String login) {
        return userRepository.getUserLoginCount(login);
    }

    @Override
    public UserDTO createOrganizer(Form form) {
        var entity = new User(form.getLogin(), form.getHash(), form.getSalt(), UserType.ORGANIZER, form.getEmail(), false);
        var newUser = userRepository.save(entity);
        return DTOMapper.toDTO(newUser);
    }

    @Override
    public PaginatedList<UserDTO> getUsers(int pageIndex) throws ApsiValidationException {
        if (pageIndex < 0)
            throw new ApsiValidationException("Indeks strony nie może być ujemny", "pageIndex");
        var page = userRepository.getUsers(PageRequest.of(pageIndex, PAGE_SIZE));

        var items = page.stream().map(DTOMapper::toDTO).collect(Collectors.toList());

        return new PaginatedList<>(items, pageIndex, page.getTotalElements(), page.getTotalPages());
    }

    @Override
    public Void deleteUser(Long id) throws ApsiException {
        var user = userRepository.findById(id);
        switch (user.get().getType()) {
            case PERSON:
                // delete user tickets
                ticketService.deleteByHolderId(id);
                break;
            case ORGANIZER:
                // soft delete event
                var events = eventService.getEventsByOrganizerId(id);
                for (EventDTO event: events) {
                    eventService.cancel(event.getId());
                }
                break;
            default:
                break;
        }
        user.get().setCanceled(true);
        userRepository.save(user.get());
        return null;
    }
}
