package apsi.team3.backend.controller;

import apsi.team3.backend.DTOs.Responses.ErrorResponse;
import apsi.team3.backend.exceptions.ApsiValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ApsiValidationException.class})
    protected ResponseEntity<Object> handleBackendException(ApsiValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(Map.of(ex.getMessage(), ex.key)));
    }
}
