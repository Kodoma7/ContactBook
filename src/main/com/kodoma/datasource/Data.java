package main.com.kodoma.datasource;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import main.com.kodoma.exceptions.BoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Кодома on 26.07.2017.
 */
@JacksonXmlRootElement(localName = "contactbook")
public class Data {
    private long id = 0;
    @JacksonXmlElementWrapper(localName = "user", useWrapping = false)
    private List<User> user = new ArrayList<>();
    private Map<Long, User> users = new HashMap<Long, User>();
    private List<Group> groups = new ArrayList<Group>();

    public Data() {
    }

    public Data(long id, Map<Long, User> users) {
        this.id = id;
        this.users = users;
    }

    /**
     * <p>Увеличивает значение id на 1 в случае добавления контакта
     * если контакт уже есть или значение id больше Long.MAX_VALUE кидается исключение</p>
     * @return id
     * @throws BoundException
     */
    public long getAvalableId() throws BoundException {
        if (id < Long.MAX_VALUE && !users.containsKey(id))
            return id++;
        else if (id < Long.MAX_VALUE)
            return getFreeId();
        else throw new BoundException();
    }

    /**
     * <p>Ищет и получает номер id из доступных</p>
     * @return id
     */
    private long getFreeId() {
        long nextId;
        long result = id;
        for (long id : users.keySet()) {
            nextId = id;
            if (!users.containsKey(++nextId)) {
                result = nextId;
                break;
            }
        }
        return result;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public Map<Long, User> getUsers() {
        return users;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", users=" + user +
                '}';
    }
}
