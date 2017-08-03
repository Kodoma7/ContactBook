package main.com.kodoma.dao.daoMAP;

/**
 * Created by Кодома on 01.08.2017.
 */
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Arrays;

@JacksonXmlRootElement(localName = "ContactBook")
public class Employees {
    @JacksonXmlElementWrapper(localName = "User", useWrapping = false)
    private Employee[] User;

    public Employees() {

    }
    public Employees(Employee[] employee) {
        this.User = employee;
    }

    public Employee[] getUser() {
        return User;
    }

    public void setUser(Employee[] user) {
        this.User = user;
    }

    @Override
    public String toString() {
        return "Employees{" +
                "employees=" + Arrays.toString(User) +
                '}';
    }
}
