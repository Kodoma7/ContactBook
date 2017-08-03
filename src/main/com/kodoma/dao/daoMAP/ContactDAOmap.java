package main.com.kodoma.dao.daoMAP;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import main.com.kodoma.dao.DAO;
import main.com.kodoma.datasource.Data;
import main.com.kodoma.datasource.User;
import main.com.kodoma.exceptions.WrongIDFormat;


import java.io.File;
import java.util.Observer;

/**
 * Created by Кодома on 29.07.2017.
 */
public class ContactDAOmap implements DAO<User> {
    public static ContactDAOmap instance;

    private ContactDAOmap() {
    }

    public static ContactDAOmap getInstance() {
        if (instance == null) {
            instance = new ContactDAOmap();
        }
        return instance;
    }

    @Override
    public void add(User obj) throws Exception {

    }

    @Override
    public void edit(User obj, String str) throws Exception {

    }

    @Override
    public void remove(User obj) throws Exception {

    }

    @Override
    public void show(User obj) throws Exception {

    }

    @Override
    public void showAll() throws Exception {
        System.out.println("работает ContactDAOmap - showAll");

        ObjectMapper objectMapper = new XmlMapper();
        Employees employees = objectMapper.readValue(new File("ContactBook.xml"), Employees.class);
        System.out.println(employees);
    }

    @Override
    public void label(long id, String nameOfGroup) throws WrongIDFormat {

    }

    @Override
    public void deleteLabel(long id) throws WrongIDFormat {

    }

    @Override
    public void setObserver(Observer o) {

    }

    @Override
    public void setData(Data data) {

    }
}
