package pl.kowalecki.dietplanner.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.kowalecki.dietplanner.model.DTO.IngredientUnit;
import pl.kowalecki.dietplanner.model.DTO.MealType;
import pl.kowalecki.dietplanner.model.DTO.MeasurementType;
import pl.kowalecki.dietplanner.model.DTO.meal.IngredientNameDTO;
import pl.kowalecki.dietplanner.model.SimpleEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MealStarterPackMapper {

    @Mapping(target = "key", source = "id")
    @Mapping(target = "value", source = "ingredientName")
    default SimpleEntry mapIngredientNameDTO(IngredientNameDTO ingredient) {
        return new SimpleEntry(String.valueOf(ingredient.getId()), ingredient.getIngredientName());
    }

    @Mapping(target = "key", source = "id")
    @Mapping(target = "value", source = "mealTypePl")
    default SimpleEntry mapMealType(MealType mealType) {
        return new SimpleEntry(String.valueOf(mealType.getId()), mealType.getMealTypePl());
    }

    @Mapping(target = "key", source = "id")
    @Mapping(target = "value", source = "shortName")
    default SimpleEntry mapIngredientUnit(IngredientUnit ingredientUnit) {
        return new SimpleEntry(String.valueOf(ingredientUnit.getId()), ingredientUnit.getShortName());
    }

    @Mapping(target = "key", source = "id")
    @Mapping(target = "value", source = "namePL")
    default SimpleEntry mapMeasurementType(MeasurementType measurementType) {
        return new SimpleEntry(String.valueOf(measurementType.getId()), measurementType.getNamePL());
    }

    default <T> List<SimpleEntry> mapList(List<T> list, Function<T, SimpleEntry> mapper) {
        return list.stream().map(mapper).collect(Collectors.toList());
    }
}

