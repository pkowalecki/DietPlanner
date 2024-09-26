package pl.kowalecki.dietplanner.model.ingredient;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IngredientName {

    private Long id;
    private String name;
    private String brand;
    private int protein;
    private int carbohydrates;
    private int fat;
    private int kcal;
}
