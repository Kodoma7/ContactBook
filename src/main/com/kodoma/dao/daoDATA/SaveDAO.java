package main.com.kodoma.dao.daoDATA;

import main.com.kodoma.datasource.Data;

import java.io.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Кодома on 05.08.2017.
 */
public class SaveDAO extends Observable {
    private Data data;
    public static SaveDAO instance;

    private SaveDAO() {
    }

    public static SaveDAO getInstance() {
        if (instance == null) {
            instance = new SaveDAO();
        }
        return instance;
    }

    public void save() throws IOException {
        String file = "ContactBook.txt";

        ObjectOutputStream out;
        out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(data);

        setChanged();
        notifyObservers("Список контактов сохранен");
    }

    public void load() throws IOException, ClassNotFoundException {
        String file = "ContactBook.txt";

        ObjectInputStream in;
            in = new ObjectInputStream(new FileInputStream(file));
            Data newData = (Data)in.readObject();
            this.data.setId(newData.getId());
            this.data.setUsers(newData.getUsers());
            this.data.setGroups(newData.getGroups());

        setChanged();
        notifyObservers("Список контактов загружен");
    }

    public void setObserver(Observer o) {
        this.addObserver(o);
    }

    public void setData(Data data) {
        this.data = data;
    }
}
