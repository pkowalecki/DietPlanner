package pl.kowalecki.dietplanner.model.page;

import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

public class FoodBoardPageData {

    Integer id;
    Double multiplier;
    List<Long> mealValues = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
    }

    public List<Long> getMealValues() {
        return mealValues;
    }

    public void setMealValues(List<Long> mealValues) {
        this.mealValues = mealValues;
    }

    @Override
    public String toString() {
        return "FoodBoardPageData{" +
                "id=" + id +
                ", multiplier=" + multiplier +
                ", mealValues=" + mealValues +
                '}';
    }
}
