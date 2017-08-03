package main.com.kodoma.exceptions;


/**
 * Created by ОСО on 17.07.17.
 */
public class SaveError extends Exception {
    @Override
    public String getMessage() {
        return "-не удалось сохранить список контактов-\n";
    }
}
