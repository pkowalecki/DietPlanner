package pl.kowalecki.dietplanner.utils;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextTools {

    public static boolean isValidEmail(String email){
        if (email == null) return false;
        boolean result = true;
        try{
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        }catch (AddressException e){
            result = false;
        }
        return result;
    }

    public static boolean isTextLengthOk(String text, int min, int max){
        if (text == null) return false;
        int length = text.length();
        return length >= min && length<=max;
    }

    /*
    * At least 1 number [0-9]
    * At least 1 small letter [a-z]
    * At least 1 big letter [A-Z]
    * At least 1 spec. char "!@#&()–[{}]:;',?/*~$^+=<>"
    * Min 8 max 20 chars {8,20}
    * */
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
    private static final Pattern passPattern = Pattern.compile(PASSWORD_PATTERN);
    public static boolean passwordPatternValidate(String password){
        try{
            Matcher matcher = passPattern.matcher(password);
            return matcher.matches();
        }catch (Exception e){
            return false;
        }
    }


}
