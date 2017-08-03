package main.com.kodoma.services;

import main.com.kodoma.dao.DAO;
import main.com.kodoma.dao.FactoryDAO;
import main.com.kodoma.datasource.Data;
import main.com.kodoma.datasource.User;
import main.com.kodoma.exceptions.BoundException;
import main.com.kodoma.exceptions.WrongIDFormat;

import java.util.Observer;

/**
 * Created by Кодома on 26.07.2017.
 */
public class UserService implements Service<User> {
    private DAO contactDAO = FactoryDAO.getDAO(User.class);

    public static UserService instance;

    /**
     * <p>Приватный конструктор, UserService - синглтон</p>
     */
    private UserService() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }
    /**
     * <p>Добавление пользователя</p>
     * @param user Contact
     * @throws BoundException
     */
    public void addUser(User user) throws Exception {
        contactDAO = FactoryDAO.getDAO(User.class);
        contactDAO.add(user);
    }

    /**
     * <p>Редактирование контакта</p>
     * @param user Contact
     * @throws WrongIDFormat
     */
    public void editUser(User user) throws Exception {
        contactDAO = FactoryDAO.getDAO(User.class);
        contactDAO.edit(user, null);
    }

    /**
     * <p>Удаление контакта</p>
     * @param user Contact
     * @throws WrongIDFormat
     */
    public void removeUser(User user) throws Exception {
        contactDAO = FactoryDAO.getDAO(User.class);
        contactDAO.remove(user);
    }

    /**
     * <p>Показать контакт</p>
     * @param user Contact
     * @throws WrongIDFormat
     */
    public void showUser(User user) throws Exception {
        contactDAO = FactoryDAO.getDAO(User.class);
        contactDAO.show(user);
    }

    /**
     * <p>Показать все контакты</p>
     */
    public void showAll() throws Exception {
        contactDAO = FactoryDAO.getDAO(User.class);
        contactDAO.showAll();
    }

    /**
     * <p>Занести контакт в группу</p>
     * @param id Контакта
     * @param name Имя группы
     * @throws WrongIDFormat
     */
    public void labelUser(long id, String name) throws Exception {
        contactDAO = FactoryDAO.getDAO(User.class);
        contactDAO.label(id, name);
    }

    /**
     *<p>Удалить контакт из группы</p>
     * @param id Контакта
     * @throws WrongIDFormat
     */
    public void deleteLabel(long id) throws Exception {
        contactDAO = FactoryDAO.getDAO(User.class);
        contactDAO.deleteLabel(id);
    }

    /**
     * <p>Установить ContactDAO наблюдателя</p>
     * @param o Наблюдатель
     */
    public void setObserver(Observer o) {
        contactDAO.setObserver(o);
    }

    /**
     * <p>Установить ContactDAO объект data</p>
     * @param data
     */
    public void setData(Data data) {
        contactDAO.setData(data);
    }
}
