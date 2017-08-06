package main.com.kodoma.services;


import main.com.kodoma.dao.daoDATA.SaveDAO;
import main.com.kodoma.datasource.Data;

import java.io.IOException;
import java.util.Observer;


/**
 * Created by Кодома on 05.08.2017.
 */
public class SaveService {
    private SaveDAO saveDAO = SaveDAO.getInstance();

    public static SaveService instance;

    private SaveService() {
    }

    public static SaveService getInstance() {
        if (instance == null) {
            instance = new SaveService();
        }
        return instance;
    }

    public void save() throws IOException {
        saveDAO.save();
    }

    public void load() throws IOException, ClassNotFoundException {
        saveDAO.load();
    }

    public void setObserver(Observer o) {
        saveDAO.setObserver(o);
    }

    public void setData(Data data) {
        saveDAO.setData(data);
    }
}
