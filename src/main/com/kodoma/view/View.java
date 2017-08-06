package main.com.kodoma.view;

import main.com.kodoma.dao.FactoryDAO;
import main.com.kodoma.util.ConsoleHelper;
import main.com.kodoma.commands.Commands;
import main.com.kodoma.controller.Controller;
import main.com.kodoma.datasource.Group;
import main.com.kodoma.datasource.User;
import main.com.kodoma.exceptions.WrongPhoneFormat;
import main.com.kodoma.util.Messages;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Кодома on 26.07.2017.
 */
public class View implements Observer {
    private Controller controller;

    public void selectParser() {
        try {
            ConsoleHelper.writeMessage("Выберите реализацию xml-парсера: DATA, DOM, SAX, MAP");
            String result = ConsoleHelper.readString();
            FactoryDAO.setDAO(result);
            ConsoleHelper.writeMessage("выбран " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void select() {
        try {
            while (true) {
            ConsoleHelper.initMenu();
            update("Выберите команду:");

                int command = ConsoleHelper.readInt();
                if (command == 0) break;

                switch (Commands.values()[command - 1]) {
                    case ADDUSER: {
                        update(Messages.CREATEUSER);
                        update(Messages.ENTERFIRSTNAME);
                        String firstName = ConsoleHelper.readString();
                        update(Messages.ENTERLASTTNAME);
                        String lastName = ConsoleHelper.readString();
                        update(Messages.ENTERADDRESS);
                        String address = ConsoleHelper.readString();
                        update(Messages.ENTERPHONENUMBER);

                        boolean wrongPhone = true;
                        int phoneNumber = 0;

                        while (wrongPhone) {
                            try {
                                phoneNumber = ConsoleHelper.readPhoneNumber();
                            } catch (WrongPhoneFormat e) {
                                update(e.getMessage());
                                continue;
                            }
                            wrongPhone = false;
                        }

                        User user = new User(0, firstName, lastName, address, phoneNumber);
                        controller.addUser(user);
                        break;
                    }
                    case EDITUSER: {
                        update(Messages.EDITCONTACT);
                        update(Messages.ENTERID);
                        long id = ConsoleHelper.readLong();
                        update(Messages.ENTERFIRSTNAME);
                        String fname = ConsoleHelper.readString();
                        update(Messages.ENTERLASTTNAME);
                        String lname = ConsoleHelper.readString();
                        update(Messages.ENTERADDRESS);
                        String address = ConsoleHelper.readString();
                        update(Messages.ENTERPHONENUMBER);

                        boolean wrongPhone = true;
                        int phoneNumber = 0;

                        while (wrongPhone) {
                            try {
                                phoneNumber = ConsoleHelper.readPhoneNumber();
                            } catch (WrongPhoneFormat e) {
                                ConsoleHelper.writeMessage(e.getMessage());
                                continue;
                            }
                            wrongPhone = false;
                        }

                        User user = new User(id, fname, lname, address, phoneNumber);
                        controller.editUser(user);
                        break;
                    }
                    case REMOVEUSER: {
                        update(Messages.REMOVECONTACT);
                        update(Messages.ENTERID);
                        long id = ConsoleHelper.readLong();

                        User user = new User(id, null, null, null, 0);
                        controller.removeUser(user);
                        break;
                    }
                    case SHOWUSER: {
                        update(Messages.SHOWCONTACT);
                        update(Messages.ENTERID);
                        long id = ConsoleHelper.readLong();

                        User user = new User(id, null, null, null, 0);
                        controller.showUser(user);
                        break;
                    }
                    case SHOWUSERS: {
                        update(Messages.SHOWALLCONTACTS);
                        controller.showAll();
                        break;
                    }
                    case LABELUSER: {
                        update(Messages.LABELCONTACT);
                        update(Messages.ENTERID);
                        long id = ConsoleHelper.readLong();
                        update(Messages.ENTERGROUP);
                        String name = ConsoleHelper.readString();

                        controller.labelUser(id, name);
                        break;
                    }
                    case DELETELABEL: {
                        update(Messages.DELETELABELCONTACT);
                        update(Messages.ENTERID);
                        long id = ConsoleHelper.readLong();

                        controller.deleteLabel(id);
                        break;
                    }
                    case EDITGROUP: {
                        update(Messages.EDITGROUPCONTACT);
                        update(Messages.ENTERGROUP);
                        String name = ConsoleHelper.readString();
                        Group group = new Group(name, null);
                        update(Messages.ENTERNEWGROUP);
                        String newName = ConsoleHelper.readString();

                        controller.editGroup(group, newName);
                        break;
                    }
                    case REMOVEGROUP: {
                        update(Messages.REMOVEGROUPCONTACT);
                        update(Messages.ENTERGROUP);
                        String name = ConsoleHelper.readString();
                        Group group = new Group(name, null);

                        controller.removeGroup(group);
                        break;
                    }
                    case SHOWUSERSOFGROUP: {
                        update(Messages.SHOWUSERSGROUP);
                        update(Messages.ENTERGROUP);
                        String name = ConsoleHelper.readString();
                        Group group = new Group(name, null);

                        controller.showUsersOfGroup(group);
                        break;
                    }
                    case SHOWGROUPS: {
                        update("Просмотр всех групп...");
                        controller.showAllGroups();
                        break;
                    }
                    case SAVE: {
                        update("Сохраниение списка контактов...");
                        controller.save();
                        break;
                    }
                    case LOAD: {
                        update("Загрузка списка контактов...");
                        controller.load();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            update(e.getMessage());
        }
    }

    public void update(String info) {
        ConsoleHelper.writeMessage(info);
    }

    @Override
    public void update(Observable o, Object arg) {
        String message = (String)arg;
        update(message);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
