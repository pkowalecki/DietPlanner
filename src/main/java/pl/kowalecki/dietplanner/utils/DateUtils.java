package pl.kowalecki.dietplanner.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DateUtils {

    public static String parseDateToddmmyy(LocalDateTime date){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return date.format(formatter);
        }catch (Exception e){
            throw new RuntimeException("Invalid date format");
        }
    }

    public static List<String> getDaysOfWeek(){
        return List.of(
                "Poniedziałek",
                "Wtorek",
                "Środa",
                "Czwartek",
                "Piątek",
                "Sobota",
                "Niedziela"
                );

    }
}
