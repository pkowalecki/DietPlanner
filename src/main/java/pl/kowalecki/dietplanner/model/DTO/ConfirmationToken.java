package pl.kowalecki.dietplanner.model.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConfirmationToken {
    private String token;
}
