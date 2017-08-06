package main.com.kodoma.dao.daoDOM;

import main.com.kodoma.dao.DAO;
import main.com.kodoma.datasource.Data;
import main.com.kodoma.datasource.User;
import main.com.kodoma.exceptions.BoundException;
import main.com.kodoma.exceptions.WrongContactName;
import main.com.kodoma.exceptions.WrongIDFormat;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Кодома on 29.07.2017.
 */
public class ContactDAOdom extends Observable implements DAO<User> {
    public static ContactDAOdom instance;

    private ContactDAOdom() {
    }

    public static ContactDAOdom getInstance() {
        if (instance == null) {
            instance = new ContactDAOdom();
        }
        return instance;
    }

    private void checkFileExists() throws IOException {
        File file = new File("contactbook.xml");
        if (!file.exists())
            file.createNewFile();
    }

    private long getAvalableId(long id, List<Long> list) throws BoundException {
        if (id < Long.MAX_VALUE && !list.contains(id))
            return id++;
        else if (id < Long.MAX_VALUE)
            return getFreeId(list);
        else throw new BoundException();
    }

    private long getFreeId(List<Long> list) {
        long nextId;
        long result = 0;
        for (long id : list) {
            nextId = id;
            if (!list.contains(++nextId)) {
                result = nextId;
                break;
            }
        }
        return result;
    }

    private List<Long> getListID(Document document) throws XPathExpressionException {
        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression expr = xpath.compile("contactbook/user/id");

        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        List<Long> idList = new ArrayList<>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            long id = Long.parseLong(n.getTextContent());
            idList.add(id);
        }
        return idList;
    }

    private List<String> getListNames(Document document) throws XPathExpressionException {
        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression expr = xpath.compile("contactbook/user/first_name");

        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        List<String> namesList = new ArrayList<>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            namesList.add(n.getTextContent());
        }
        return namesList;
    }

    private Document getDocument(String name) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(name);
        return document;
    }

    private void writeDocument(Document document) throws TransformerFactoryConfigurationError, TransformerException, FileNotFoundException {
        Transformer tr = TransformerFactory.newInstance().newTransformer();
        DOMSource source = new DOMSource(document);
        FileOutputStream fos = new FileOutputStream("contactbook.xml");
        StreamResult result = new StreamResult(fos);
        tr.transform(source, result);
    }

    @Override
    public void add(User user) throws Exception {
        checkFileExists();
        Document document = getDocument("contactbook.xml");

        List<Long> idList = getListID(document);

        long avalableID = getAvalableId(0, idList);

        Node root = document.getDocumentElement();
        Element book = document.createElement("user");

        Element title = document.createElement("id");
        title.setTextContent(avalableID + "");
        Element author = document.createElement("first_name");
        author.setTextContent(user.getFname());

        Element date = document.createElement("last_name");
        date.setTextContent(user.getLname());

        Element isbn = document.createElement("address");
        isbn.setTextContent(user.getAddress());

        Element publisher = document.createElement("phone_number");
        publisher.setTextContent(user.getPhoneNumber() + "");

        Element cost = document.createElement("group");
        cost.setTextContent(user.getGroup());

        book.appendChild(title);
        book.appendChild(author);
        book.appendChild(date);
        book.appendChild(isbn);
        book.appendChild(publisher);
        book.appendChild(cost);

        root.appendChild(book);

        writeDocument(document);
    }

    @Override
    public void edit(User user, String str) throws Exception {
        Document document = getDocument("contactbook.xml");
        List<Long> listID = getListID(document);

        if (!listID.contains(user.getId())) throw new WrongIDFormat();

        NodeList languages = document.getElementsByTagName("user");
        Element lang;
        // проходим по каждому элементу User
        for(int i = 0; i < languages.getLength(); i++) {
            lang = (Element) languages.item(i);
            Node nameID = lang.getElementsByTagName("id").item(0).getFirstChild();
            long id = Long.parseLong(nameID.getTextContent());

            if (id == user.getId()) {
                Node firstName = lang.getElementsByTagName("first_name").item(0).getFirstChild();
                Node lastName = lang.getElementsByTagName("last_name").item(0).getFirstChild();
                Node address = lang.getElementsByTagName("address").item(0).getFirstChild();
                Node phoneNumber = lang.getElementsByTagName("phone_number").item(0).getFirstChild();
                Node group = lang.getElementsByTagName("group").item(0).getFirstChild();

                firstName.setNodeValue(user.getFname());
                lastName.setNodeValue(user.getLname());
                address.setNodeValue(user.getAddress());
                phoneNumber.setNodeValue(user.getPhoneNumber() + " ");
                group.setNodeValue(user.getGroup());
            }
        }
        writeDocument(document);
    }

    @Override
    public void remove(User user) throws Exception {
        Document document = getDocument("contactbook.xml");

        // Получаем корневой элемент
        Node contactbookNode = document.getFirstChild();
        NodeList users = contactbookNode.getChildNodes();

        for (int i = 0; i < users.getLength(); i++) {
            Node nextNode = users.item(i);

            if (nextNode.getNodeName().equals("user")) {
                System.out.println(nextNode.getNodeName());

                Element userElement = (Element) nextNode;
                Node ID = userElement.getElementsByTagName("id").item(0);
                long id = Long.parseLong(ID.getTextContent());

                if (id == user.getId())
                    contactbookNode.removeChild(nextNode);
            }
        }
        writeDocument(document);
    }

    @Override
    public void show(User user) throws Exception {
        Document document = getDocument("contactbook.xml");

        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression expr = xpath.compile("contactbook/user");

        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        Element element;

        for (int i = 0; i < nodes.getLength(); i++) {
            Node userNode = nodes.item(i);
            NodeList userList = userNode.getChildNodes();

            Node tagNode = userList.item(1);
            long id = Long.parseLong(tagNode.getTextContent());

            if (id == user.getId()) {
                element = (Element) userNode;
                String ID = element.getElementsByTagName("id").item(0).getTextContent();
                String firstName = element.getElementsByTagName("first_name").item(0).getTextContent();
                String lastName = element.getElementsByTagName("last_name").item(0).getTextContent();
                String address = element.getElementsByTagName("address").item(0).getTextContent();
                String phoneNumber = element.getElementsByTagName("phone_number").item(0).getTextContent();
                String group = element.getElementsByTagName("group").item(0).getTextContent();

                setChanged();
                String result = String.format("Group: %s [id: %s, name: %s %s, address: %s, phone number: %s]",
                        group, ID, firstName, lastName, address, phoneNumber);
                notifyObservers(result);
            }
        }
    }

    @Override
    public void showAll() throws IOException, SAXException, ParserConfigurationException {
        Document document = getDocument("contactbook.xml");
        NodeList users = document.getElementsByTagName("user");

        Element element;

        for (int i = 0; i < users.getLength(); i++) {
            element = (Element) users.item(i);
            String ID = element.getElementsByTagName("id").item(0).getTextContent();
            String firstName = element.getElementsByTagName("first_name").item(0).getTextContent();
            String lastName = element.getElementsByTagName("last_name").item(0).getTextContent();
            String address = element.getElementsByTagName("address").item(0).getTextContent();
            String phoneNumber = element.getElementsByTagName("phone_number").item(0).getTextContent();
            String group = element.getElementsByTagName("group").item(0).getTextContent();

            setChanged();
            String result = String.format("Group: %s [id: %s, name: %s %s, address: %s, phone number: %s]",
                    group, ID, firstName, lastName, address, phoneNumber);
            notifyObservers(result);
        }
    }

    @Override
    public void searchUser(String name) throws Exception {
        Document document = getDocument("contactbook.xml");
        if (!getListNames(document).contains(name)) throw new WrongContactName();

        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression expr = xpath.compile("contactbook/user");

        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        Element element;

        for (int i = 0; i < nodes.getLength(); i++) {
            Node userNode = nodes.item(i);

            element = (Element) userNode;
            String firstName = element.getElementsByTagName("first_name").item(0).getTextContent();

            if (firstName.equals(name)) {
                String ID = element.getElementsByTagName("id").item(0).getTextContent();
                String lastName = element.getElementsByTagName("last_name").item(0).getTextContent();
                String address = element.getElementsByTagName("address").item(0).getTextContent();
                String phoneNumber = element.getElementsByTagName("phone_number").item(0).getTextContent();
                String group = element.getElementsByTagName("group").item(0).getTextContent();

                setChanged();
                String result = String.format("Group: %s [id: %s, name: %s %s, address: %s, phone number: %s]",
                        group, ID, firstName, lastName, address, phoneNumber);
                notifyObservers(result);
            }
        }
    }

    @Override
    public void label(long id, String nameOfGroup) throws Exception {
        Document document = getDocument("contactbook.xml");
        List<Long> listID = getListID(document);

        if (!listID.contains(id)) throw new WrongIDFormat();

        NodeList languages = document.getElementsByTagName("user");
        Element lang;

        for(int i = 0; i < languages.getLength(); i++) {
            lang = (Element) languages.item(i);
            Node nameID = lang.getElementsByTagName("id").item(0).getFirstChild();
            long idL = Long.parseLong(nameID.getTextContent());

            if (idL == id) {
                Node group = lang.getElementsByTagName("group").item(0).getFirstChild();
                group.setNodeValue(nameOfGroup);
            }
        }
        writeDocument(document);
    }

    @Override
    public void deleteLabel(long id) throws Exception {
        Document document = getDocument("contactbook.xml");

        // Получаем корневой элемент
        Node contactbookNode = document.getFirstChild();
        NodeList users = contactbookNode.getChildNodes();

        for (int i = 0; i < users.getLength(); i++) {
            Node nextNode = users.item(i);

            if (nextNode.getNodeName().equals("user")) {
                Element userElement = (Element) nextNode;
                Node ID = userElement.getElementsByTagName("id").item(0);
                long idUser = Long.parseLong(ID.getTextContent());

                if (idUser == id) {
                    Node group = userElement.getElementsByTagName("group").item(0).getFirstChild();;
                    System.out.println(group);
                    group.setNodeValue(" ");
                }
            }
        }
        writeDocument(document);
    }

    @Override
    public void setObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void setData(Data data) {

    }
}
