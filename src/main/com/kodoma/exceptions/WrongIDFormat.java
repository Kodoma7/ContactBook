package main.com.kodoma.exceptions;

/**
 * Created by ОСО on 14.07.17.
 */
public class WrongIDFormat extends Exception {
    @Override
    public String getMessage() {
        return "-контакт с указанным ID не существует-\n";
    }
}
