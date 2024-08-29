package pl.kowalecki.dietplanner.model.ingredient;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.kowalecki.dietplanner.model.ingredient.ingredientAmount.IngredientAmount;
import pl.kowalecki.dietplanner.model.ingredient.ingredientAmount.IngredientUnit;
import pl.kowalecki.dietplanner.model.ingredient.ingredientMeasurement.IngredientMeasurement;
import pl.kowalecki.dietplanner.model.ingredient.ingredientMeasurement.MeasurementType;
import pl.kowalecki.dietplanner.model.Meal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {
    private Long ingredientId;
    private Double ingredientAmount;
    private IngredientUnit ingredientUnit;
    private Double measurementValue;
    private MeasurementType measurementType;
    private IngredientName ingredientNameId;
    @JsonBackReference
    private Meal meal;

    public Double sumTotalAmount(double d1, double d2){
        return (d1+d2);
    }

    public static IngredientAmount getOnlyDoubleFromAmount(String totalAmount){
        totalAmount = totalAmount.replaceAll("\\s", "");
        String regex = "([0-9]+([,.][0-9]+)?)([a-zA-Z]+)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(totalAmount);

        if (matcher.find()){
            String amount = matcher.group(1);
            String unit = matcher.group(3);

            amount = amount.replace(",", ".");
            double value = Double.parseDouble(amount);
//            return new IngredientAmount(value, unit);
        }
        return new IngredientAmount();
    }

    public static IngredientMeasurement getOnlyDoubleFromMeasurement(String totalAmount){
        if(totalAmount == null){
            return new IngredientMeasurement();
        }
//        totalAmount = totalAmount.replaceFirst("\\s", "");
//        String regex = "([0-9]+([,.][0-9]+)?)([a-zA-Z]+)";
//        String regex = "([0-9]+([,.][0-9]+)?)([a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+)";
        String regex = "([0-9]+([.,][0-9]+)?)\\s+(.*)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(totalAmount);

        if (matcher.find()){
            String amount = matcher.group(1);
            String unit = matcher.group(3);

            amount = amount.replace(",", ".");
            double value = Double.parseDouble(amount);
//            return new IngredientMeasurement(value, "sztuki");
        }
        return new IngredientMeasurement();
    }
}
