package util;

public class InputValidator {
    public static boolean isValid(String input){
        return input != null && !input.trim().isEmpty();
    }

    public static boolean isNumeric(String input){
        if (!isValid(input)) return false;
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
}
