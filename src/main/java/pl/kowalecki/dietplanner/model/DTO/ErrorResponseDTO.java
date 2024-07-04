package pl.kowalecki.dietplanner.model.DTO;

import java.util.Map;

public class ErrorResponseDTO {
    private Map<String, String> errors;

    public ErrorResponseDTO(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
