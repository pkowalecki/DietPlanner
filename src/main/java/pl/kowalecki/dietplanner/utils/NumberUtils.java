package pl.kowalecki.dietplanner.utils;

public class NumberUtils {
    public static boolean isDoubleLengthOk(Double number, Double min, Double max) {
        if(number == null || number < min){
            return false;
        }else return number < max;
    }
}
