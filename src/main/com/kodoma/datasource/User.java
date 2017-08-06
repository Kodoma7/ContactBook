package main.com.kodoma.datasource;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.Serializable;

/**
 * Created by Кодома on 26.07.2017.
 */

public class User implements Serializable {
    @JacksonXmlProperty(localName = "id")
    private long id;
    @JacksonXmlProperty(localName = "first_name")
    private String fname;
    @JacksonXmlProperty(localName = "last_name")
    private String lname;
    @JacksonXmlProperty(localName = "address")
    private String address;
    @JacksonXmlProperty(localName = "phone_number")
    private int phoneNumber;
    @JacksonXmlProperty(localName = "group")
    private String group = " ";

    public User() {
    }

    public User(long id, String fname, String lname, String address, int phoneNumber) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (getId() != user.getId()) return false;
        if (getPhoneNumber() != user.getPhoneNumber()) return false;
        if (getFname() != null ? !getFname().equals(user.getFname()) : user.getFname() != null) return false;
        if (getLname() != null ? !getLname().equals(user.getLname()) : user.getLname() != null) return false;
        if (getAddress() != null ? !getAddress().equals(user.getAddress()) : user.getAddress() != null) return false;
        return getGroup() != null ? getGroup().equals(user.getGroup()) : user.getGroup() == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getFname() != null ? getFname().hashCode() : 0);
        result = 31 * result + (getLname() != null ? getLname().hashCode() : 0);
        result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
        result = 31 * result + getPhoneNumber();
        result = 31 * result + (getGroup() != null ? getGroup().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Group: " + group + " [id: " + id + ", name: " + fname + " " + lname + ", address: " + address + ", phone number: " + phoneNumber + "]";
    }
}
