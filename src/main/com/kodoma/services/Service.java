
package main.com.kodoma.services;

import main.com.kodoma.datasource.Data;

import java.util.Observer;

/**
 * Created by Кодома on 27.07.2017.
 */
public interface Service<T> {

    void addUser(T obj) throws Exception;

    /**
     * <p>Установить сервису наблюдателя.
     * Аналогичный метод вызывается в контроллере,
     * Наблюдатель устанавливатся для DAO</p>
     * @param o
     */
    void setObserver(Observer o);

    /**
     * <p>Установить сервису объект data.
     * Аналогичный метод вызывается в контроллере,
     * Data устанавливатся для DAO</p>
     * @param data
     */
    void setData(Data data);
}