package pl.kowalecki.dietplanner.model.DTO.meal;

import lombok.*;
import pl.kowalecki.dietplanner.model.DTO.MeasurementDTO;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealBoardDTO {
    private String name;
    private String brand;
    private double ingredientAmount;
    private String ingredientUnit;
    private List<MeasurementDTO> measurementList;
}

