package main.com.kodoma.dao;

import main.com.kodoma.dao.daoDATA.ContactDAO;
import main.com.kodoma.dao.daoDATA.GroupDAO;
import main.com.kodoma.dao.daoDOM.ContactDAOdom;
import main.com.kodoma.dao.daoDOM.GroupDAOdom;
import main.com.kodoma.dao.daoMAP.ContactDAOmap;
import main.com.kodoma.dao.daoMAP.GroupDAOmap;
import main.com.kodoma.dao.daoSAX.ContactDAOsax;
import main.com.kodoma.dao.daoSAX.GroupDAOsax;
import main.com.kodoma.datasource.Group;
import main.com.kodoma.datasource.User;

/**
 * Created by Кодома on 26.07.2017.
 */
public class FactoryDAO {
    private static String str = "";
    public static FactoryDAO instance;

    private FactoryDAO() {
    }

    public static FactoryDAO getInstance() {
        if (instance == null) {
            instance = new FactoryDAO();
        }
        return instance;
    }

    public static DAO getDAO(Object obj) {
        DAO dao = ContactDAO.getInstance();
        if (obj.equals(User.class) && str.equals("DATA")) dao = ContactDAO.getInstance();
        else if (obj.equals(Group.class) && str.equals("DATA")) dao = GroupDAO.getInstance();

        else if (obj.equals(User.class) && str.equalsIgnoreCase("DOM")) dao = ContactDAOdom.getInstance();
        else if (obj.equals(Group.class) && str.equalsIgnoreCase("DOM")) dao = GroupDAOdom.getInstance();

        else if (obj.equals(User.class) && str.equalsIgnoreCase("SAX")) dao = ContactDAOsax.getInstance();
        else if (obj.equals(Group.class) && str.equalsIgnoreCase("SAX")) dao = GroupDAOsax.getInstance();

        else if (obj.equals(User.class) && str.equalsIgnoreCase("MAP")) dao = ContactDAOmap.getInstance();
        else if (obj.equals(Group.class) && str.equalsIgnoreCase("MAP")) dao = GroupDAOmap.getInstance();
        return dao;
    }

    public static void setDAO(String str) {
        FactoryDAO dao = FactoryDAO.getInstance();
        dao.str = str;
    }
}
