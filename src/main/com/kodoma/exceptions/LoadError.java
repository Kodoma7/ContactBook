package main.com.kodoma.exceptions;


/**
 * Created by ОСО on 17.07.17.
 */
public class LoadError extends Exception {
    @Override
    public String getMessage() {
        return "-не удалось загрузить список контактов-\n";
    }
}
