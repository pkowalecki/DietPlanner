package pl.kowalecki.dietplanner.model.ingredient;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientKey {

    private String name;
    private String description;

    public IngredientKey(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
