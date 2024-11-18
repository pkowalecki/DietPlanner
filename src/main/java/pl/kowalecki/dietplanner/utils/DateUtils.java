package pl.kowalecki.dietplanner.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static String parseDateToddmmyy(LocalDateTime date){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return date.format(formatter);
        }catch (Exception e){
            throw new RuntimeException("Invalid date format");
        }
    }
}
