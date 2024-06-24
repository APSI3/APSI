package apsi.team3.backend.controller;

import apsi.team3.backend.DTOs.FormDTO;
import apsi.team3.backend.DTOs.PaginatedList;
import apsi.team3.backend.DTOs.Requests.CreateFormRequest;
import apsi.team3.backend.DTOs.Requests.FormRejectionRequest;
import apsi.team3.backend.DTOs.UserDTO;
import apsi.team3.backend.exceptions.ApsiException;
import apsi.team3.backend.exceptions.ApsiValidationException;
import apsi.team3.backend.interfaces.IFormService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forms")
@CrossOrigin(origins = {"http://localhost:3000"}, allowCredentials = "true")
public class FormController {
    private final IFormService formService;

    @Autowired
    public FormController(IFormService formService) { this.formService = formService; }

    @PostMapping()
    public ResponseEntity<FormDTO> create(@RequestBody CreateFormRequest request) throws ApsiException {
        var newForm = formService.create(request);
        return ResponseEntity.ok(newForm);
    }

    @GetMapping()
    public ResponseEntity<PaginatedList<FormDTO>> getForms(
            @RequestParam int pageIndex
    ) throws ApsiValidationException {
        var resp = formService.getForms(pageIndex);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/accept")
    public ResponseEntity<UserDTO> accept(@RequestParam Long formId) throws MessagingException, ApsiValidationException {
        var resp = formService.accept(formId);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/reject")
    public ResponseEntity<Boolean> reject(@RequestBody FormRejectionRequest rejectionRequest) throws MessagingException, ApsiValidationException {
        formService.reject(rejectionRequest);
        return ResponseEntity.ok(true);
    }
}
