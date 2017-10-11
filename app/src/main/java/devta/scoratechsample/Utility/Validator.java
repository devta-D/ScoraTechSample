package devta.scoratechsample.Utility;

/**
 * @author Divyanshu Tayal
 */
public class Validator {

    public static boolean isValidEmail(String inputEmail){
        return inputEmail.length()>0 && inputEmail.toLowerCase().trim().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
    }

}
