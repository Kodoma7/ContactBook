package main.com.kodoma.dao.daoMAP;

import main.com.kodoma.dao.DAO;
import main.com.kodoma.datasource.Data;
import main.com.kodoma.datasource.Group;
import main.com.kodoma.exceptions.WrongIDFormat;

import java.util.Observer;

/**
 * Created by Кодома on 29.07.2017.
 */
public class GroupDAOmap implements DAO<Group> {
    public static GroupDAOmap instance;

    private GroupDAOmap() {
    }

    public static GroupDAOmap getInstance() {
        if (instance == null) {
            instance = new GroupDAOmap();
        }
        return instance;
    }

    @Override
    public void add(Group obj) throws Exception {

    }

    @Override
    public void edit(Group obj, String str) throws Exception {

    }

    @Override
    public void remove(Group obj) throws Exception {

    }

    @Override
    public void show(Group obj) throws Exception {

    }

    @Override
    public void showAll() {

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
