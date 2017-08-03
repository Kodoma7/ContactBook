package main.com.kodoma.services;

import main.com.kodoma.dao.DAO;
import main.com.kodoma.dao.FactoryDAO;
import main.com.kodoma.datasource.Data;
import main.com.kodoma.datasource.Group;
import main.com.kodoma.exceptions.WrongGroupName;

import java.util.Observer;

/**
 * Created by Кодома on 26.07.2017.
 */
public class GroupService {
    private final DAO groupDAO = FactoryDAO.getDAO(Group.class);

    public static GroupService instance;

    /**
     * <p>Приватный конструктор, GroupService - синглтон</p>
     */
    private GroupService() {
    }

    public static GroupService getInstance() {
        if (instance == null) {
            instance = new GroupService();
        }
        return instance;
    }

    /**
     * <p>Редактирование группы</p>
     * @param group Group
     * @param newName Новое имя группы
     * @throws WrongGroupName
     */
    public void editGroup(Group group, String newName) throws Exception {
        groupDAO.edit(group, newName);
    }

    /**
     * <p>Удаление группы</p>
     * @param group Group
     * @throws WrongGroupName
     */
    public void removeGroup(Group group) throws Exception {
        groupDAO.remove(group);
    }

    /**
     * <p>Показать все контакты выбранной группы</p>
     * @param group Group
     * @throws WrongGroupName
     */
    public void showUsersOfGroup(Group group) throws Exception {
        groupDAO.show(group);
    }

    /**
     * <p>Показать все группы</p>
     */
    public void showAllGroups() throws Exception {
        groupDAO.showAll();
    }

    /**
     * <p>Установить GroupDAO наблюдателя</p>
     * @param o Наблюдатель
     */
    public void setObserver(Observer o) {
        groupDAO.setObserver(o);
    }
    /**
     * <p>Установить GroupDAO объект data</p>
     * @param data
     */
    public void setData(Data data) {
        groupDAO.setData(data);
    }
}
