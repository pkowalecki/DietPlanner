package pl.kowalecki.dietplanner.model.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FoodBoardPageRequest {
    Double multiplier;
    List<Long> mealIds;
}
