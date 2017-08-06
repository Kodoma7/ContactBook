package main.com.kodoma.exceptions;

/**
 * Created by Кодома on 06.08.2017.
 */
public class WrongContactName extends Exception {
    @Override
    public String getMessage() {
        return "-контакт с указанным именем не существует-\n";
    }
}
