package apsi.team3.backend.interfaces;

import apsi.team3.backend.DTOs.FormDTO;
import apsi.team3.backend.DTOs.Requests.CreateFormRequest;
import apsi.team3.backend.exceptions.ApsiException;

public interface IFormService {
    FormDTO create(CreateFormRequest request) throws ApsiException;
}
