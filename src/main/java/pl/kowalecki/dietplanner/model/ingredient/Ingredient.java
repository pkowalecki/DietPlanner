package pl.kowalecki.dietplanner.model.ingredient;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import pl.kowalecki.dietplanner.model.ingredient.ingredientAmount.IngredientAmount;
import pl.kowalecki.dietplanner.model.ingredient.ingredientAmount.IngredientUnit;
import pl.kowalecki.dietplanner.model.ingredient.ingredientMeasurement.IngredientMeasurement;
import pl.kowalecki.dietplanner.model.ingredient.ingredientMeasurement.MeasurementType;
import pl.kowalecki.dietplanner.model.Meal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientId;
    private String name;

    private Double ingredientAmount;

    @Enumerated(EnumType.STRING)
    private IngredientUnit ingredientUnit;

    private Double measurementValue;

    @Enumerated(EnumType.STRING)
    private MeasurementType measurementType;



    @ManyToOne
    @JoinColumn(name = "meal_id")
    @JsonBackReference
    private Meal meal;


    public Ingredient(String name, Double ingredientAmount, IngredientUnit ingredientUnit, Double measurementValue, MeasurementType measurementType) {
        this.name = name;
        this.ingredientAmount = ingredientAmount;
        this.ingredientUnit = ingredientUnit;
        this.measurementValue = measurementValue;
        this.measurementType = measurementType;
    }

    public Ingredient() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(Double measurementValue) {
        this.measurementValue = measurementValue;
    }

    public MeasurementType getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(MeasurementType measurementType) {
        this.measurementType = measurementType;
    }

    public Double getIngredientAmount() {
        return ingredientAmount;
    }

    public void setIngredientAmount(Double ingredientAmount) {this.ingredientAmount = ingredientAmount;}

    public IngredientUnit getIngredientUnit() {
        return ingredientUnit;
    }

    public void setIngredientUnit(IngredientUnit ingredientUnit) {
        this.ingredientUnit = ingredientUnit;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    @Transient
    public Double sumTotalAmount(double d1, double d2){
        return d1+d2;
    }
    @Transient
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

    @Transient
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

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }
}
