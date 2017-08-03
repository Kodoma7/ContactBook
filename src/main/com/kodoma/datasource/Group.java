package main.com.kodoma.datasource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Кодома on 26.07.2017.
 */
public class Group {
    private String nameGroup;
    private List<User> list = new ArrayList<User>();

    /**
     * <p>Конструктор, принимает имя группы и контакт</p>
     * @param nameGroup имя группы
     * @param user контакт
     */
    public Group(String nameGroup, User user) {
        this.nameGroup = nameGroup;
        addAtGroup(user);
    }

    /**
     * <p>Добавить контакт в группу</p>
     * @param user
     */
    public void addAtGroup(User user) {
        user.setGroup(nameGroup);
        list.add(user);
    }

    /**
     * <p>Удалить контакт из группы</p>
     * @param user Контакт
     */
    public void deleteFromGroup(User user) {
        user.setGroup(" ");
        list.remove(user);
    }

    /**
     * <p>Удалить группу</p>
     * @return Group
     */
    public Group deleteGroup() {
        int size = list.size();

        for (int i = 0; i < size; i++) {
            deleteFromGroup(list.get(0));
        }
        return this;
    }

    /**
     * <p>Переименовать группу</p>
     * @param newName Новое имя группы
     * @return Group
     */
    public Group renameGroup(String newName) {
        this.nameGroup = newName;
        for (User user : list) {
            user.setGroup(nameGroup);
        }
        return this;
    }

    public String getNameGroup() {
        return nameGroup;
    }

    public List<User> getList() {
        return list;
    }

    /**
     * <p>Вывод в строку всех пользователей из Group</p>
     * @return
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (User user : list) {
            result.append(user.toString()).append("\n");
        }
        return result.toString();
    }
}