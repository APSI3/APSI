package apsi.team3.backend.contoller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import apsi.team3.backend.DTOs.ApiResponse;
import apsi.team3.backend.exceptions.ApsiValidationException;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ ApsiValidationException.class })
    protected ResponseEntity<Object> handleBackendException(ApsiValidationException ex) {
        return new ResponseEntity<>(new ApiResponse<>(ex.getMessage(), ex.key), HttpStatus.BAD_REQUEST);
    }
}
