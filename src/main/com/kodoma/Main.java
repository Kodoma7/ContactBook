package main.com.kodoma;

import main.com.kodoma.controller.Controller;
import main.com.kodoma.datasource.Data;
import main.com.kodoma.util.ValidatorXML;
import main.com.kodoma.view.View;

/**
 * Created by Кодома on 26.07.2017.
 */
public class Main {
    public static void main(String[] args) {
        Data data = new Data();
        View view = new View();

        ValidatorXML validator = new ValidatorXML();
        validator.setView(view);
        validator.validateXMLSchema("ContactBook.xsd", "ContactBook.xml");

        view.selectParser();
        Controller controller = new Controller();

        controller.setData(data);
        controller.setObserver(view);
        view.setController(controller);

        view.select();
    }
}
