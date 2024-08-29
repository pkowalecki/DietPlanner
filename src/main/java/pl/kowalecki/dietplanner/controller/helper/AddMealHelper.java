package pl.kowalecki.dietplanner.controller.helper;

import org.springframework.stereotype.Component;
import pl.kowalecki.dietplanner.model.DTO.meal.AddMealRequestDTO;

import java.util.HashMap;
import java.util.Map;

@Component
public class AddMealHelper {

    HashMap<String, String> errors = new HashMap<>();

    public Map<String, String> checkData(AddMealRequestDTO addMealRequestDTO) {
        errors = new HashMap<>();
        return errors;
    }
}
