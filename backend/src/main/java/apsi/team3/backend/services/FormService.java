package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.FormDTO;
import apsi.team3.backend.DTOs.PaginatedList;
import apsi.team3.backend.DTOs.Requests.CreateFormRequest;
import apsi.team3.backend.DTOs.Requests.FormRejectionRequest;
import apsi.team3.backend.DTOs.UserDTO;
import apsi.team3.backend.exceptions.ApsiException;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IFormService;
import apsi.team3.backend.model.Form;
import apsi.team3.backend.model.FormStatus;
import apsi.team3.backend.model.User;
import apsi.team3.backend.model.UserType;
import apsi.team3.backend.repository.FormRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class FormService implements IFormService {
    private final FormRepository formRepository;
    private final UserService userService;
    private final MailService mailService;
    private final int PAGE_SIZE = 10;

    @Autowired
    public FormService(FormRepository formRepository, UserService userService, MailService mailService) {
        this.formRepository = formRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    @Override
    public FormDTO create(CreateFormRequest request) throws ApsiException {
        var salt = UserService.generateSalt();
        var hash = UserService.hashPassword(request.getPassword(), salt);
        var entity = new Form(
                request.getLogin(),
                salt,
                hash,
                FormStatus.PENDING.toString(),
                request.getEmail()
        );
        var newForm = formRepository.save(entity);
        return DTOMapper.toDTO(newForm);
    }

    @Override
    public PaginatedList<FormDTO> getForms(int pageIndex) throws ApsiValidationException {
        if (pageIndex < 0)
            throw new ApsiValidationException("Indeks strony nie może być ujemny", "pageIndex");
        var page = formRepository.getForms(PageRequest.of(pageIndex, PAGE_SIZE));

        var items = page
                .stream()
                .map(DTOMapper::toDTO)
                .collect(Collectors.toList());

        return new PaginatedList<>(items, pageIndex, page.getTotalElements(), page.getTotalPages());
    }

    @Override
    public UserDTO accept(Long formId) throws ApsiValidationException, MessagingException {
        var form = formRepository.findById(formId);
        if (!form.get().getStatus().equals(FormStatus.PENDING.toString())){
            throw new ApsiValidationException("Wniosek został już przetworzony", "status");
        }
        var loginCount = userService.getUserLoginCount(form.get().getLogin());
        if (loginCount > 0) {
            throw new ApsiValidationException("Użytkownik o takim loginie już istnieje", "login");
        }

        form.get().setStatus(FormStatus.ACCEPTED.toString());
        formRepository.save(form.get());
        var user = userService.createOrganizer(form.get());

        mailService.sendMail(
            form.get().getEmail(),
            "Twoje zgłoszenie zostało zaakceptowane",
            String.format("Admin pozytywnie rozpatrzył Twoje zgłoszenie i założył Ci konto na login %s", form.get().getLogin())
        );

        return user;
    }

    @Override
    public void reject(FormRejectionRequest rejectionRequest) throws MessagingException, ApsiValidationException {
        var form = formRepository.findById(rejectionRequest.getId());
        if (!form.get().getStatus().equals(FormStatus.PENDING.toString())){
            throw new ApsiValidationException("Wniosek został już przetworzony", "status");
        }
        form.get().setStatus(FormStatus.REJECTED.toString());
        formRepository.save(form.get());

        mailService.sendMail(
            form.get().getEmail(),
            "Twoje zgłoszenie zostało odrzucone",
            String.format("Admin odrzucił Twoje zgłoszenie.\n\nPowód odrzucenia:\n%s", rejectionRequest.getCause())
        );
    }
}
