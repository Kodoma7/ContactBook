package main.com.kodoma.dao.daoDATA;

import main.com.kodoma.dao.DAO;
import main.com.kodoma.datasource.Data;
import main.com.kodoma.datasource.Group;
import main.com.kodoma.exceptions.WrongGroupName;
import main.com.kodoma.exceptions.WrongIDFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Кодома on 26.07.2017.
 */
public class GroupDAO extends Observable implements DAO<Group> {
    private Data data;
    public static GroupDAO instance;

    private GroupDAO() {

    }

    public static GroupDAO getInstance() {
        if (instance == null) {
            instance = new GroupDAO();
        }
        return instance;
    }

    public void add(Group obj) throws Exception {

    }

    /**
     * <p>Собрать имена всех групп</p>
     * @return ArrayList<String>
     */
    private ArrayList<String> listNamesOfGroups() {
        ArrayList<String> list = new ArrayList<String>();
        for (Group group : data.getGroups())
            list.add(group.getNameGroup());
        return list;
    }

    /**
     * <p>Редактирование группы</p>
     * @param obj Group
     * @param newName Новое имя группы
     * @throws WrongGroupName
     */
    public void edit(Group obj, String newName) throws WrongGroupName {
        if (!listNamesOfGroups().contains(obj.getNameGroup())) throw new WrongGroupName();

        for (Group group : data.getGroups()) {
            if (group.getNameGroup().equals(obj.getNameGroup())) {
                group.renameGroup(newName);
            }
        }

        setChanged();
        notifyObservers("Группа " + newName + " отредактирована.");
    }

    /**
     * <p>Удаление группы. Для каждого контакта из указанной группы
     * стирается имя группы, список контактов List<Contact> в классе Group обнуляется</p>
     * @param obj Group
     * @throws WrongGroupName
     */

    public void remove(Group obj) throws WrongGroupName {
        String nameOfGroup = obj.getNameGroup();
        List<Group> groups = data.getGroups();
        if (!listNamesOfGroups().contains(nameOfGroup)) throw new WrongGroupName();
        Group result = null;

        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            if (group.getNameGroup().equals(nameOfGroup)) {
                group.deleteGroup();
                result = groups.remove(i);
            }
        }

        setChanged();
        notifyObservers("Группа " + result.getNameGroup() + " удалена.");
    }

    /**
     * <p>Показать контакты выбранной группы</p>
     * @param obj Group
     * @throws WrongGroupName
     */
    public void show(Group obj) throws WrongGroupName {
        if (!listNamesOfGroups().contains(obj.getNameGroup())) throw new WrongGroupName();

        for (Group group : data.getGroups()) {
            if (group.getNameGroup().equals(group.getNameGroup())) {
                setChanged();
                notifyObservers(group.toString());
            }
        }
    }

    /**
     * <p>Показать список всех групп</p>
     */
    public void showAll() {
        List<Group> groups = data.getGroups();
        for (Group group : groups) {
            setChanged();
            notifyObservers(group.getNameGroup());
        }
    }

    public void label(long id, String nameOfGroup) throws WrongIDFormat {

    }

    public void deleteLabel(long id) throws WrongIDFormat {

    }

    public void setObserver(Observer o) {
        this.addObserver(o);
    }

    public void setData(Data data) {
        this.data = data;
    }
}
