package main.com.kodoma;

import main.com.kodoma.controller.Controller;
import main.com.kodoma.datasource.Data;
import main.com.kodoma.view.View;

/**
 * Created by Кодома on 26.07.2017.
 */
public class Main {
    public static void main(String[] args) {
        Data data = new Data();
        View view = new View();
        view.selectParser();
        Controller controller = new Controller();

        controller.setData(data);
        controller.setObserver(view);
        view.setController(controller);

        view.select();
    }
}
