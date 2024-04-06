package apsi.team3.backend.DTOs.Responses;

import lombok.Value;

import java.util.Map;

@Value
public class ErrorResponse {
    Map<String, String> errors;
}
