package pl.kowalecki.dietplanner.model.ingredient.ingredientAmount;

public class IngredientAmount {
    Double value;
    IngredientUnit unit;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public IngredientUnit getUnit() {
        return unit;
    }

    public void setUnit(IngredientUnit unit) {
        this.unit = unit;
    }

    public IngredientAmount(Double value, IngredientUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    public IngredientAmount() {
    }


    public String getIngredientAmountAsString(){
        return value.toString() + " " + unit.getShortName();
    }
}
