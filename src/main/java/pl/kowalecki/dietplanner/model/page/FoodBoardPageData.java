package pl.kowalecki.dietplanner.model.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FoodBoardPageData {

    Double multiplier;
    List<Long> meals = new ArrayList<>();
    Boolean isSnackMultiplied;

}
