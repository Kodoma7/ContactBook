package main.com.kodoma.dao.daoDOM;

import main.com.kodoma.dao.DAO;
import main.com.kodoma.datasource.Data;
import main.com.kodoma.datasource.User;
import main.com.kodoma.exceptions.BoundException;
import main.com.kodoma.exceptions.WrongIDFormat;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * Created by Кодома on 29.07.2017.
 */
public class ContactDAOdom implements DAO<User> {
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
        File file = new File("ContactBook.xml");
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
        XPathExpression expr = xpath.compile("ContactBook/User/ID");

        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        List<Long> idList = new ArrayList<>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            long id = Long.parseLong(n.getTextContent());
            idList.add(id);
        }
        return idList;
    }

    private Document getDocument(String name) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        // Создается дерево DOM документа из файла
        Document document = documentBuilder.parse(name);
        return document;
    }

    private static void writeDocument(Document document) throws TransformerFactoryConfigurationError {
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            FileOutputStream fos = new FileOutputStream("ContactBook.xml");
            StreamResult result = new StreamResult(fos);
            tr.transform(source, result);
        } catch (TransformerException | IOException e) {
            System.out.println("exception");
        }
    }

    @Override
    public void add(User user) throws Exception {
        System.out.println("работает ContactDAOdom");
        checkFileExists();
        Document document = getDocument("ContactBook.xml");

        List<Long> idList = getListID(document);

        long avalableID = getAvalableId(0, idList);

        Node root = document.getDocumentElement();
        Element book = document.createElement("User");

        Element title = document.createElement("ID");
        title.setTextContent(avalableID + "");
        Element author = document.createElement("FirstName");
        author.setTextContent(user.getFname());

        Element date = document.createElement("LastName");
        date.setTextContent(user.getLname());

        Element isbn = document.createElement("Address");
        isbn.setTextContent(user.getAddress());

        Element publisher = document.createElement("PhoneNumber");
        publisher.setTextContent(user.getPhoneNumber() + "");

        Element cost = document.createElement("Group");
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
        System.out.println("работает ContactDAOdom - edit");
        Document document = getDocument("ContactBook.xml");
        List<Long> listID = getListID(document);

        if (!listID.contains(user.getId())) throw new WrongIDFormat();

        NodeList languages = document.getElementsByTagName("User");
        Element lang;
        // проходим по каждому элементу User
        for(int i=0; i<languages.getLength();i++) {
            lang = (Element) languages.item(i);
            Node nameID = lang.getElementsByTagName("ID").item(0).getFirstChild();
            long id = Long.parseLong(nameID.getTextContent());

            if (id == user.getId()) {
                Node firstName = lang.getElementsByTagName("FirstName").item(0).getFirstChild();
                firstName.setNodeValue(user.getFname());
            }
        }
        writeDocument(document);
    }

    @Override
    public void remove(User user) throws Exception {
        System.out.println("работает ContactDAOdom - remove");
        Document document = getDocument("ContactBook.xml");

        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression expr = xpath.compile("ContactBook");

        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node userNode = nodes.item(i);
            NodeList list = nodes.item(i).getChildNodes(); //тэги
            Node n = list.item(1);

            Element element = (Element)n;
            Node ID = element.getElementsByTagName("ID").item(0);

            System.out.println(ID.getNodeName());
            System.out.println(ID.getTextContent());

            long id = Long.parseLong(ID.getTextContent());

            if (id == user.getId())
                userNode.removeChild(n);
        }
        writeDocument(document);
    }

    @Override
    public void show(User user) throws Exception {
        Document document = getDocument("ContactBook.xml");

        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression expr = xpath.compile("ContactBook/User");

        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node userNode = nodes.item(i); //Contact
            NodeList userList = userNode.getChildNodes(); //Users

            Node tagNode = userList.item(1); //Tag
            long id = Long.parseLong(tagNode.getTextContent());

            if (id == user.getId()) {
                System.out.println(userNode.getTextContent());
            }
        }
    }

    @Override
    public void showAll() {
        try {
            Document document = getDocument("ContactBook.xml");
            NodeList users = document.getElementsByTagName("User");

            for (int i = 0; i < users.getLength(); i++) {
                System.out.println(users.item(i).getTextContent());
            }

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void label(long id, String nameOfGroup) throws Exception {
        System.out.println("работает ContactDAOdom - edit");
        Document document = getDocument("ContactBook.xml");
        List<Long> listID = getListID(document);

        if (!listID.contains(id)) throw new WrongIDFormat();

        NodeList languages = document.getElementsByTagName("User");
        Element lang;
        // проходим по каждому элементу Contact
        for(int i=0; i<languages.getLength();i++) {
            lang = (Element) languages.item(i);
            Node nameID = lang.getElementsByTagName("ID").item(0).getFirstChild();
            long idL = Long.parseLong(nameID.getTextContent());

            if (idL == id) {
                Node group = lang.getElementsByTagName("Group").item(0).getFirstChild();
                group.setNodeValue(nameOfGroup);
            }
        }
        writeDocument(document);
    }

    @Override
    public void deleteLabel(long id) throws Exception {
        System.out.println("работает ContactDAOdom - deleteLabel");
        System.out.println("работает ContactDAOdom - edit");
        Document document = getDocument("ContactBook.xml");
        List<Long> listID = getListID(document);

        if (!listID.contains(id)) throw new WrongIDFormat();

        NodeList languages = document.getElementsByTagName("ContactBook");
        Element lang;
        // проходим по каждому элементу Contact
        for(int i=0; i<languages.getLength();i++) {
            lang = (Element) languages.item(i);
            Node nameID = lang.getElementsByTagName("ID").item(0).getFirstChild();
            long idL = Long.parseLong(nameID.getTextContent());

            if (idL == id) {
                Node group = lang.getElementsByTagName("Group").item(0).getFirstChild();
                group.setNodeValue(" ");
            }
        }
        writeDocument(document);
    }

    @Override
    public void setObserver(Observer o) {

    }

    @Override
    public void setData(Data data) {

    }
}
