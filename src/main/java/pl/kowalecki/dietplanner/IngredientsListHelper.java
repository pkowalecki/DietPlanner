package pl.kowalecki.dietplanner;

import pl.kowalecki.dietplanner.model.ingredient.Ingredient;

import java.util.*;
import java.util.stream.Collectors;

public class IngredientsListHelper {
    public static List<Ingredient> prepareIngredientsList(List<Ingredient> ingredients, Double multiplier){
        Map<String, Ingredient> ingredientMap = new HashMap<>();
        for (Ingredient newIngredient: ingredients){
            String ingredientNameKey = newIngredient.getName() + newIngredient.getIngredientUnit().getShortName();
            String measurementNameKey = newIngredient.getName() + newIngredient.getMeasurementType().getMeasurementName();
            if (ingredientMap.containsKey(ingredientNameKey)){
                Ingredient existingIngredient = ingredientMap.get(ingredientNameKey);
                if(newIngredient.getName().equals(existingIngredient.getName())){
                    if (newIngredient.getIngredientUnit().equals(existingIngredient.getIngredientUnit())){
                        existingIngredient.setIngredientAmount(existingIngredient.sumTotalAmount(newIngredient.getIngredientAmount(),existingIngredient.getIngredientAmount()));
                        existingIngredient.setIngredientUnit(existingIngredient.getIngredientUnit());
                        ingredientMap.put(ingredientNameKey, existingIngredient);
                    }else {
                        ingredientMap.put(ingredientNameKey, existingIngredient);
                    }
                }
            } else{
                newIngredient.setIngredientAmount(newIngredient.getIngredientAmount()*multiplier);
                newIngredient.setMeasurementValue(newIngredient.getMeasurementValue()*multiplier);
                ingredientMap.put(ingredientNameKey, newIngredient);
            }

//            if (ingredientMap.containsKey(measurementNameKey)){
//                Ingredient existingMeasurement = ingredientMap.get(measurementNameKey);
//                if(newIngredient.getName().equals(existingMeasurement.getName())){
//                    if (newIngredient.getMeasurementType().equals(existingMeasurement.getMeasurementType())){
//                        existingMeasurement.setMeasurementValue(existingMeasurement.sumTotalAmount(newIngredient.getMeasurementValue(),existingMeasurement.getMeasurementValue()));
//                        existingMeasurement.setMeasurementType(existingMeasurement.getMeasurementType());
//                        ingredientMap.put(measurementNameKey, existingMeasurement);
//                    }else {
//                        ingredientMap.put(measurementNameKey, existingMeasurement);
//                    }
//                }
//            }else{
//                newIngredient.setMeasurementValue(newIngredient.getMeasurementValue()*multiplier);
//                ingredientMap.put(measurementNameKey, newIngredient);
//            }




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
