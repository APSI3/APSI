package apsi.team3.backend.DTOs;

import java.util.*;

public class ApiResponse<T> {
    public T data;
    public Map<String, String> errors;
    public boolean success;

    public ApiResponse(T data) {
        this.data = data;
        success = true;
        errors = null;
    }

    public ApiResponse(Map<String, String> errors) {
        this.errors = errors;
        success = false;
        data = null;
    }

    public ApiResponse(String error, String key) {
        errors = new HashMap<String, String>() {{ 
            put(key, error);
        }};
        success = false;
        data = null;
    }
}
