package apsi.team3.backend.services;

import apsi.team3.backend.DTOs.DTOMapper;
import apsi.team3.backend.DTOs.FormDTO;
import apsi.team3.backend.DTOs.Requests.CreateFormRequest;
import apsi.team3.backend.exceptions.ApsiException;
import apsi.team3.backend.interfaces.IFormService;
import apsi.team3.backend.model.Form;
import apsi.team3.backend.model.FormStatus;
import apsi.team3.backend.repository.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormService implements IFormService {
    private final FormRepository formRepository;

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
}
