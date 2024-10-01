package pl.kowalecki.dietplanner.model.enums;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MealTypeDTO {
    private String mealTypePl;
    private String mealTypeEn;
}