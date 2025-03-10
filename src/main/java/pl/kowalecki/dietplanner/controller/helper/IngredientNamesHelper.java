package pl.kowalecki.dietplanner.controller.helper;

import org.springframework.stereotype.Component;
import pl.kowalecki.dietplanner.model.ingredient.IngredientName;
import pl.kowalecki.dietplanner.utils.NumberUtils;
import pl.kowalecki.dietplanner.utils.TextTools;

import java.util.HashMap;
import java.util.Map;

@Component
public class IngredientNamesHelper {

    HashMap<String, String> errors = new HashMap<>();

    public Map<String, String> checkIngredients(IngredientName ingredient) {
        errors = new HashMap<>();
        checkIngredientName(ingredient.getName());
        checkIngredientBrand(ingredient.getBrand());
        checkProteins(ingredient.getProtein());
        checkCarbohydrates(ingredient.getCarbohydrates());
        checkFat(ingredient.getFat());
        checkKcal(ingredient.getKcal());
        return errors;
    }

    private void checkIngredientName(String name) {
        if (!TextTools.isTextLengthOk(name, 3, 254))
            errors.put(IngredientNamePole.INGREDIENT_NAME.getFieldName(), "Nazwa składnika powinna mieć co najmniej 3 znaki");
    }

    private void checkIngredientBrand(String brand) {
        if (brand == null || brand.isEmpty()) return;
        if (!TextTools.isTextLengthOk(brand, 3, 254))
            errors.put(IngredientNamePole.INGREDIENT_BRAND.getFieldName(), "Marka składnika powinna mieć co najmniej 3 znaki");

    }

    private void checkProteins(double protein) {
        if (!NumberUtils.isDoubleLengthOk(protein, 0.0, 1000.0))
            errors.put(IngredientNamePole.PROTEIN.getFieldName(), "Zawartość białka powinna być między 0.0 a 1000.0");
    }

    private void checkCarbohydrates(double carbohydrates) {
        if (!NumberUtils.isDoubleLengthOk(carbohydrates, 0.0, 1000.0))
            errors.put(IngredientNamePole.CARBOHYDRATES.getFieldName(), "Zawartość węgli powinna być między 0.0 a 1000.0");
    }

    private void checkFat(double fat) {
        if (!NumberUtils.isDoubleLengthOk(fat, 0.0, 1000.0))
            errors.put(IngredientNamePole.PROTEIN.getFieldName(), "Zawartość tłuszczu powinna być między 0.0 a 1000.0");
    }

    private void checkKcal(double kcal) {
        if (!NumberUtils.isDoubleLengthOk(kcal, 0.0, 9999.9))
            errors.put(IngredientNamePole.KCAL.getFieldName(), "Wartość kcal powinna być między 0.0 a 9999.0");
    }
}
