package main.com.kodoma.dao.daoMAP;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
public class Employee {
    @JacksonXmlProperty(localName = "id")
    private String id;
    @JacksonXmlProperty(localName = "first_name")
    private String firstName;
    @JacksonXmlProperty(localName = "last_name")
    private String lastName;
    @JacksonXmlProperty(localName = "age")
    private int age;

    public Employee() {
    }

    public Employee(String id, String firstName, String lastName, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                '}';
    }
}
