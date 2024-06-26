package apsi.team3.backend.interfaces;

import apsi.team3.backend.DTOs.FormDTO;
import apsi.team3.backend.DTOs.PaginatedList;
import apsi.team3.backend.DTOs.Requests.CreateFormRequest;
import apsi.team3.backend.DTOs.Requests.FormRejectionRequest;
import apsi.team3.backend.DTOs.UserDTO;
import apsi.team3.backend.exceptions.ApsiException;
import apsi.team3.backend.exceptions.ApsiValidationException;
import jakarta.mail.MessagingException;

public interface IFormService {
    FormDTO create(CreateFormRequest request) throws ApsiException;
    PaginatedList<FormDTO> getForms(int pageIndex) throws ApsiValidationException;
    UserDTO accept(Long formId) throws ApsiValidationException, MessagingException;

    void reject(FormRejectionRequest rejectionRequest) throws MessagingException, ApsiValidationException;
}
