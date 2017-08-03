package main.com.kodoma.dao.daoDATA;

import main.com.kodoma.dao.DAO;
import main.com.kodoma.datasource.Data;
import main.com.kodoma.datasource.Group;
import main.com.kodoma.datasource.User;
import main.com.kodoma.exceptions.BoundException;
import main.com.kodoma.exceptions.WrongIDFormat;

import java.util.*;

/**
 * Created by Кодома on 26.07.2017.
 */
public class ContactDAO extends Observable implements DAO<User> {
    private Data data;
    public static ContactDAO instance;

    private ContactDAO() {
    }

    public static ContactDAO getInstance() {
        if (instance == null) {
            instance = new ContactDAO();
        }
        return instance;
    }

    /**
     * <p>Добавление контакта в map<Long, Contact>,
     * при добавлении генерируется наименьший возможный id.
     * уведомление View об изменении</p>
     * @param obj Contact
     * @throws BoundException
     */

    public void add(User obj) throws BoundException {
        data.getUsers().put(data.getAvalableId(), obj);
        setChanged();
        notifyObservers("Пользователь " + obj + " добавлен.");
    }

    /**
     * <p>Редактирвание контакта, уведомление View об изменении</p>
     * @param obj Contact
     * @param str null
     * @throws WrongIDFormat
     */

    public void edit(User obj, String str) throws WrongIDFormat {
        if (!data.getUsers().containsKey(obj.getId())) throw new WrongIDFormat();

        data.getUsers().put(data.getId(), obj);
        setChanged();
        notifyObservers("Пользователь " + obj + " изменен.");
    }

    /**
     * <p>Удаление контакта, id уменьшаетмя на 1</p>
     * @param obj Contact
     * @throws WrongIDFormat
     */

    public void remove(User obj) throws WrongIDFormat {
        if (!data.getUsers().containsKey(obj.getId())) throw new WrongIDFormat();

        User user = data.getUsers().remove(obj.getId());
        long idremove = data.getId();
        data.setId(idremove--);

        setChanged();
        notifyObservers("Пользователь " + user + " удален.");
    }

    /**
     * <p>Показать выбранный контакт, в Contact переопределен метод toString()</p>
     * @param obj Contact
     * @throws WrongIDFormat
     */
    public void show(User obj) throws WrongIDFormat {
        if (!data.getUsers().containsKey(obj.getId())) throw new WrongIDFormat();
        User user = data.getUsers().get(obj.getId());

        notifyObservers(user.toString());
    }

    /**
     * <p>Показать все контакты</p>
     */
    public void showAll() {
        Collection<User> usersList = data.getUsers().values();
        for (User user : usersList) {
            setChanged();
            notifyObservers(user.toString());
        }
    }

    /**
     * <p>Занести контакт в группу, Group - класс, в котором: ArrayList<Contact> имеет поле типа String
     * для хранения информации о названии группы</p>
     * @param id Контакта
     * @param nameOfGroup Имя группы
     * @throws WrongIDFormat
     */
    public void label(long id, String nameOfGroup) throws WrongIDFormat {
        Map<Long, User> users = data.getUsers();
        List<Group> groups = data.getGroups();
        if (!users.containsKey(id)) throw new WrongIDFormat();

        User user = users.get(id);

        int size = groups.size();
        boolean isExist = false;

        for (int i = 0; i < size; i++) {
            if (nameOfGroup.equals(groups.get(i).getNameGroup())) {
                groups.get(i).addAtGroup(user);
                isExist = true;
            }
        }
        if (groups.isEmpty() || isExist == false) groups.add(new Group(nameOfGroup, user));

        setChanged();
        notifyObservers("Пользователь " + user + " добавлен в группу " + user.getGroup() + ".");
    }

    /**
     * <p>Удалить контакт из группы</p>
     * @param id Контакта
     * @throws WrongIDFormat
     */
    public void deleteLabel(long id) throws WrongIDFormat {
        Map<Long, User> users = data.getUsers();
        List<Group> groups = data.getGroups();
        if (!users.containsKey(id)) throw new WrongIDFormat();

        User user = users.get(id);
        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            if (user.getGroup().equals(group.getNameGroup())) {
                group.deleteFromGroup(user);
                if (group.getList().isEmpty()) groups.remove(group);
            }
        }

        setChanged();
        notifyObservers("Пользователь " + user + " удален из группы.");
    }

    public void setObserver(Observer o) {
        this.addObserver(o);
    }

    public void setData(Data data) {
        this.data = data;
    }
}
