package pl.kowalecki.dietplanner.model.ingredient.ingredientMeasurement;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum MeasurementType {

    OPAKOWANIE("opakowanie"),
    PORCJA("porcja"),
    KOSTKA("kostka"),
    LYZKA("łyżka"),
    LYZECZKA("łyżeczka"),
    PUSZKA("puszka"),
    SZTUKA("sztuka"),
    KROMKA("kromka"),
    SZKLANKA("szklanka"),
    SLOIK("słoik"),
    GARSC("garść"),
    PLASTER("plaster"),
    SZCZYPTA("szczypta"),
    ZABEK("ząbek")




    ;

    String name;
    MeasurementType(String name){
        this.name=name;
    }
    public String getMeasurementName(){
        return name;
    }

    public static Map<MeasurementType, List<String>> getMeasurementTypeMap(){
        Map<MeasurementType, List<String>> measurementTypeListHashMap = new HashMap<>();
        for(MeasurementType measurementType : values()){
            List<String> measurementTypes = new ArrayList<>();
            if (!measurementTypeListHashMap.containsKey(measurementType)) {
                measurementTypes.add(measurementType.getMeasurementName());
            }
            measurementTypeListHashMap.put(measurementType, measurementTypes);
        }
        return measurementTypeListHashMap;
    }

    public static MeasurementType getByName(String name) {
        for (MeasurementType measurementType : values()) {
            if (measurementType.getMeasurementName().equals(name)) return measurementType;
        }
        return null;
    }


}
