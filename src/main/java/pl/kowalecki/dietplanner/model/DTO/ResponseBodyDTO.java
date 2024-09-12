package pl.kowalecki.dietplanner.model.DTO;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseBodyDTO {

    private ResponseStatus status;
    private String message;
    private Map<String, ?> data;


    public enum ResponseStatus {
        OK,
        BAD_DATA,
        ERROR,
        UNAUTHORIZED,
        ;
    }

    public ResponseBodyDTO(ResponseStatus status){
        this.status=status;
    }


}


