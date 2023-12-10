package pl.kowalecki.dietplanner;

import pl.kowalecki.dietplanner.model.ingredient.Ingredient;

import java.util.*;
import java.util.stream.Collectors;

public class IngredientsListHelper {
    public static List<Ingredient> prepareIngredientsList(List<Ingredient> ingredients){
        Map<String, Ingredient> ingredientMap = new HashMap<>();
        for (Ingredient newIngredient: ingredients){
            String ingredientName = newIngredient.getName() + newIngredient.getIngredientUnit().getShortName();
            if (ingredientMap.containsKey(ingredientName)){ //Jak mapa nie ma tej nazwy, to od razu wrzucamy do listy zakupów.
                Ingredient existingIngredient = ingredientMap.get(ingredientName);
                if(newIngredient.getName().equals(existingIngredient.getName())){
                    if (newIngredient.getIngredientUnit().equals(existingIngredient.getIngredientUnit())){
                        existingIngredient.setIngredientAmount(existingIngredient.sumTotalAmount(newIngredient.getIngredientAmount(),existingIngredient.getIngredientAmount()));
                        existingIngredient.setIngredientUnit(existingIngredient.getIngredientUnit());
                        ingredientMap.put(ingredientName, existingIngredient);
                    }else {
                        ingredientMap.put(ingredientName, existingIngredient);
                    }
                }
            }else{
                ingredientMap.put(ingredientName, newIngredient);
            }
        }

        //Sortujemy mapkę, żeby dostać listę posortowaną alfabetycznie.
        Map<String, Ingredient> sortedMap = ingredientMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return new ArrayList<>(sortedMap.values());
    }

//    private static void checkAndAddExistingMeasurement(Ingredient ingredient, Ingredient existingIngredient) {
//        String totalMeasurement = ingredient.getIngredientMeasurement();
//
//        IngredientMeasurement measurementAmount = Ingredient.getOnlyDoubleFromMeasurement(existingIngredient.getIngredientMeasurement());
//        IngredientMeasurement newMeasurement = Ingredient.getOnlyDoubleFromMeasurement(totalMeasurement);
//
//        if(measurementAmount.getIngredientMeasurementUnit().equals(newMeasurement.getIngredientMeasurementUnit())){
//            existingIngredient.setIngredientMeasurement(existingIngredient.sumTotalAmount(measurementAmount.getIngredientMeasurementAmount(),newMeasurement.getIngredientMeasurementAmount()) +" "+ measurementAmount.getIngredientMeasurementUnit());
//        }else{
//            existingIngredient.setIngredientMeasurement(measurementAmount.getIngredientMeasurementAmount() +" "+ measurementAmount.getIngredientMeasurementUnit() + " oraz "
//                    + newMeasurement.getIngredientMeasurementAmount() +" "+ newMeasurement.getIngredientMeasurementUnit());
//        }
//    }
}
