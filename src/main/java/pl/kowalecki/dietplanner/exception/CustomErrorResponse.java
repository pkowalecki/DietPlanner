package pl.kowalecki.dietplanner.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomErrorResponse {
    private String errorCode;
    private String errorMessage;
    private long timestamp;
}
