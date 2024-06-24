package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.FormDTO;
import apsi.team3.backend.DTOs.PaginatedList;
import apsi.team3.backend.DTOs.Requests.CreateFormRequest;
import apsi.team3.backend.exceptions.ApsiException;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IFormService;
import apsi.team3.backend.model.Form;
import apsi.team3.backend.model.FormStatus;
import apsi.team3.backend.model.User;
import apsi.team3.backend.repository.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class FormService implements IFormService {
    private final FormRepository formRepository;
    private final int PAGE_SIZE = 10;

    @Autowired
    public FormService(FormRepository formRepository) { this.formRepository = formRepository; }

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
}
