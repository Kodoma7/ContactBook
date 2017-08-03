package main.com.kodoma.exceptions;

/**
 * Created by ОСО on 14.07.17.
 */
public class WrongGroupName extends Exception {
    @Override
    public String getMessage() {
        return "-указанной группы не сущесвует-\n";
    }
}
