package pl.kowalecki.dietplanner.utils;

import pl.kowalecki.dietplanner.model.DTO.IngredientUnit;
import pl.kowalecki.dietplanner.model.DTO.MealType;
import pl.kowalecki.dietplanner.model.DTO.MeasurementType;
import pl.kowalecki.dietplanner.model.DTO.meal.IngredientNameDTO;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUtils {

    public static List<Map<String, String>> mapIngredientName(List<IngredientNameDTO> ingredientNameList) {
        List<Map<String, String>> resultList = new ArrayList<>();
        for (IngredientNameDTO ingredient : ingredientNameList) {
            Map<String, String> map = new HashMap<>();
            map.put("value", ingredient.getId().toString());
            map.put("name", ingredient.getIngredientName());
            resultList.add(map);
        }
        return resultList;
    }

    public static List<Map<String, String>> mapIngredientUnit(List<IngredientUnit> ingredientUnitList) {
        List<Map<String, String>> resultList = new ArrayList<>();
        for (IngredientUnit unit : ingredientUnitList) {
            Map<String, String> map = new HashMap<>();
            map.put("value", String.valueOf(unit.getId()));
            map.put("name", unit.getShortName());
            resultList.add(map);
        }
        return resultList;
    }

    public static List<Map<String, String>> mapMealType(List<MealType> mealTypeList) {
        List<Map<String, String>> resultList = new ArrayList<>();
        for (MealType mealType : mealTypeList) {
            Map<String, String> map = new HashMap<>();
            map.put("value", String.valueOf(mealType.getId()));
            map.put("name", mealType.getMealTypePl());
            resultList.add(map);
        }
        return resultList;
    }

    public static List<Map<String, String>> mapMeasurementType(List<MeasurementType> measurementTypeList) {
        List<Map<String, String>> resultList = new ArrayList<>();
        for (MeasurementType measurementType : measurementTypeList) {
            Map<String, String> map = new HashMap<>();
            map.put("value", String.valueOf(measurementType.getId()));
            map.put("name", measurementType.getNamePL());
            resultList.add(map);
        }
        return resultList;
    }

    public static List<Map<String, String>> mapToFtl(List<?> list, String valueMethod, String nameMethod) {
        List<Map<String, String>> resultList = new ArrayList<>();
        for (Object object : list) {
            Map<String, String> map = new HashMap<>();
            map.put("value", invokeMethod(object, valueMethod));
            map.put("name", invokeMethod(object, nameMethod));
            resultList.add(map);
        }
        return resultList;
    }

    private static String invokeMethod(Object object, String methodName) {
        try {
            Method method = object.getClass().getMethod("get" + TextTools.firstToUpper(methodName));
            Object value = method.invoke(object);
            return value != null ? value.toString() : "";
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Błąd: metoda '" + "get" + TextTools.firstToUpper(methodName) + "' nie istnieje w klasie "
                    + object.getClass().getSimpleName());
        } catch (Exception e) {
            throw new RuntimeException("Błąd podczas wywołania metody '" + "get" + TextTools.firstToUpper(methodName) + "' w klasie "
                    + object.getClass().getSimpleName(), e);
        }
    }
}
