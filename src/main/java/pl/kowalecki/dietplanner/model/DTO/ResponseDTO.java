package pl.kowalecki.dietplanner.model.DTO;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDTO {

    private ResponseStatus status;
    private Map<String, String> errors;

    public enum ResponseStatus {
        OK,
        BADDATA,
        INVALID_TOKEN
        ;
    }

    public ResponseDTO(ResponseStatus status){
        this.status=status;
    }


}


