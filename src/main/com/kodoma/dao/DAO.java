package main.com.kodoma.dao;

import main.com.kodoma.datasource.Data;
import main.com.kodoma.exceptions.WrongIDFormat;

import java.util.Observer;


/**
 * Created by Кодома on 26.07.2017.
 */
public interface DAO<T> {
    /**
     * <p>Добавление пользователя</p>
     * @param obj Contact
     * @throws Exception
     */
    void add(T obj) throws Exception;

    /**
     * <p>Редактирование контакта или группы</p>
     * @param obj Contact или Group
     * @param str Новое имя группы или null
     * @throws Exception
     */
    void edit(T obj, String str) throws Exception;

    /**
     * <p>Удаление контакта или группы</p>
     * @param obj Contact или Group
     * @throws Exception
     */
    void remove(T obj) throws Exception;

    /**
     * <p>Показать контакт или контакты определенной группы</p>
     * @param obj Contact или Group
     * @throws Exception
     */
    void show(T obj) throws Exception;

    /**
     * <p>Показать все контакты или показать
     * все группы</p>
     */
    void showAll() throws Exception;

    /**
     * <p>Занести контакт в группу</p>
     * @param id Контакта
     * @param nameOfGroup Имя группы
     * @throws WrongIDFormat
     */
    void label(long id, String nameOfGroup) throws Exception;

    /**
     *<p>Удалить контакт из группы</p>
     * @param id Контакта
     * @throws WrongIDFormat
     */
    void deleteLabel(long id) throws Exception;

    /**
     * Установить DAO наблюдателя
     * @param o Наблюдатель
     */
    void setObserver(Observer o);

    /**
     * Установить DAO объект data
     * @param data
     */
    void setData(Data data);
}