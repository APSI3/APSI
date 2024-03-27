package apsi.team3.backend.DTOs;

import java.util.*;

public class ApiResponse<T> {
    public T Data;
    public Map<String, String> Errors;
    public boolean Success;

    public ApiResponse(T data) {
        Data = data;
        Success = true;
        Errors = null;
    }

    public ApiResponse(Map<String, String> errors) {
        Errors = errors;
        Success = false;
        Data = null;
    }
}
