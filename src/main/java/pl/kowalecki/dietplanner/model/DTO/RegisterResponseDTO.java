package pl.kowalecki.dietplanner.model.DTO;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponseDTO {

    private RegisterStatus status;
    private Map<String, String> errors;

    public enum RegisterStatus{
        OK,
        BADDATA,
        ;
    }

    public RegisterResponseDTO(RegisterStatus status){
        this.status=status;
    }


}


