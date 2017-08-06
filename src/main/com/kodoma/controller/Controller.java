package main.com.kodoma.controller;

import main.com.kodoma.datasource.Data;
import main.com.kodoma.datasource.Group;
import main.com.kodoma.exceptions.BoundException;
import main.com.kodoma.datasource.User;
import main.com.kodoma.exceptions.WrongGroupName;
import main.com.kodoma.exceptions.WrongIDFormat;
import main.com.kodoma.services.GroupService;
import main.com.kodoma.services.SaveService;
import main.com.kodoma.services.UserService;

import java.io.IOException;
import java.util.Observer;

/**
 * Created by Кодома on 26.07.2017.
 */
public class Controller {
    private UserService userService = UserService.getInstance();
    private GroupService groupService = GroupService.getInstance();
    private SaveService saveService = SaveService.getInstance();

    /**
     * <p>Добавление пользователя</p>
     * @param user Contact
     * @throws BoundException
     */
    public void addUser(User user) throws Exception {
        userService.addUser(user);
    }
    /**
     * <p>Редактирование контакта</p>
     * @param user Contact
     * @throws WrongIDFormat
     */
    public void editUser(User user) throws Exception {
        userService.editUser(user);
    }

    /**
     * <p>Удаление контакта</p>
     * @param user Contact
     * @throws WrongIDFormat
     */
    public void removeUser(User user) throws Exception {
        userService.removeUser(user);
    }

    /**
     * <p>Показать контакт</p>
     * @param user Contact
     * @throws WrongIDFormat
     */
    public void showUser(User user) throws Exception {
        userService.showUser(user);
    }

    /**
     * <p>Показать все контакты</p>
     */
    public void showAll() throws Exception {
        userService.showAll();
    }

    public void searchUserByName(String name) throws Exception {
        userService.searchUserByName(name);
    }

    /**
     * <p>Занести контакт в группу</p>
     * @param id Контакта
     * @param name Имя группы
     * @throws WrongIDFormat
     */
    public void labelUser(long id, String name) throws Exception {
        userService.labelUser(id, name);
    }

    /**
     *<p>Удалить контакт из группы</p>
     * @param id Контакта
     * @throws WrongIDFormat
     */
    public void deleteLabel(long id) throws Exception {
        userService.deleteLabel(id);
    }

    /**
     * <p>Редактирование группы</p>
     * @param group Group
     * @param newName Новое имя группы
     * @throws WrongGroupName
     */
    public void editGroup(Group group, String newName) throws Exception {
        groupService.editGroup(group, newName);
    }

    /**
     * <p>Удаление группы</p>
     * @param group Group
     * @throws WrongGroupName
     */
    public void removeGroup(Group group) throws Exception {
        groupService.removeGroup(group);
    }

    /**
     * <p>Показать все контакты выбранной группы</p>
     * @param group Group
     * @throws WrongGroupName
     */
    public void showUsersOfGroup(Group group) throws Exception {
        groupService.showUsersOfGroup(group);
    }

    /**
     * <p>Показать все группы</p>
     */
    public void showAllGroups() throws Exception {
        groupService.showAllGroups();
    }

    /**
     * <p>Установить контроллеру наблюдателя,
     * наблюдатель устанавливается всем DAO</p>
     * @param o Наблюдатель
     */
    public void setObserver(Observer o) {
        userService.setObserver(o);
        groupService.setObserver(o);
        saveService.setObserver(o);
    }

    /**
     * <p>Установить контроллеру объект data,
     * data устанавливается всем DAO</p>
     * @param data
     */
    public void setData(Data data) {
        userService.setData(data);
        groupService.setData(data);
        saveService.setData(data);
    }

    public void save() throws IOException {
        saveService.save();
    }

    public void load() throws IOException, ClassNotFoundException {
        saveService.load();
    }
}
