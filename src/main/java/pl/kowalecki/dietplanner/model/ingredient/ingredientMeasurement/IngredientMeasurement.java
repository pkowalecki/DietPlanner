package pl.kowalecki.dietplanner.model.ingredient.ingredientMeasurement;


public class IngredientMeasurement {
    private Double value;
    private MeasurementType measurementType;


    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public MeasurementType getIngredientUnit() {
        return measurementType;
    }

    public void setIngredientUnit(MeasurementType measurementType) {
        this.measurementType = measurementType;
    }

    public IngredientMeasurement(Double value, MeasurementType measurementType) {
        this.value = value;
        this.measurementType = measurementType;
    }

    public IngredientMeasurement() {
    }

    public String getIngredientMeasurementAsString(){
        return String.valueOf(value) + " " + measurementType.getMeasurementName();
    }
}
