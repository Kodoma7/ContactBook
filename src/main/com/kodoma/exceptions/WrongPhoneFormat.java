package main.com.kodoma.exceptions;

/**
 * Created by ОСО on 14.07.17.
 */
public class WrongPhoneFormat extends NumberFormatException {
    @Override
    public String getMessage() {
        return "-Неверный формат номера-\n" +
                "Повторите попытку ввода";
    }
}

