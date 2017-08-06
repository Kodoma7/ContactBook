package main.com.kodoma.dao.daoSAX;

import main.com.kodoma.dao.DAO;
import main.com.kodoma.datasource.Data;
import main.com.kodoma.datasource.Group;
import main.com.kodoma.datasource.User;
import main.com.kodoma.exceptions.WrongGroupName;
import main.com.kodoma.exceptions.WrongIDFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.*;
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
import java.util.HashSet;
import java.util.List;
import java.util.Observer;

/**
 * Created by Кодома on 29.07.2017.
 */
public class GroupDAOsax implements DAO<Group> {
    public static GroupDAOsax instance;

    private GroupDAOsax() {
    }

    public static GroupDAOsax getInstance() {
        if (instance == null) {
            instance = new GroupDAOsax();
        }
        return instance;
    }

    private void checkFileExists() throws IOException {
        File file = new File("ContactBook.xml");
        if (!file.exists())
            file.createNewFile();
    }

    private List<String> getGroupsNames(Document document) throws XPathExpressionException {
        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression expr = xpath.compile("ContactBook/User/Group");

        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        List<String> names = new ArrayList<>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            names.add(n.getTextContent());
        }
        return names;
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
    public void add(Group obj) throws Exception {

    }

    @Override
    public void edit(Group group, String newName) throws Exception {
        System.out.println("работает GroupDAOdom - edit");
        checkFileExists();

        Document document = getDocument("ContactBook.xml");
        List<String> names = getGroupsNames(document);

        if (!names.contains(group.getNameGroup())) throw new WrongGroupName();

        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        XPathExpression expr = xpath.compile("ContactBook/User/Group");

        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            if (group.getNameGroup().equals(n.getTextContent())) {
                n.getFirstChild().setNodeValue(newName);
            }
        }
        writeDocument(document);
    }

    @Override
    public void remove(Group group) throws Exception {
        System.out.println("работает GroupDAOsax - remove");
        checkFileExists();

        Document document = getDocument("ContactBook.xml");
        List<String> names = getGroupsNames(document);

        if (!names.contains(group.getNameGroup())) throw new WrongGroupName();

        NodeList languages = document.getElementsByTagName("User");
        Element lang;

        for (int i = 0; i < languages.getLength(); i++) {
            lang = (Element) languages.item(i);
            Node nameGroup = lang.getElementsByTagName("Group").item(0).getFirstChild();

            if (nameGroup.getTextContent().equals(group.getNameGroup())) {
                Node g = lang.getElementsByTagName("Group").item(0).getFirstChild();
                g.setNodeValue(" ");
            }
        }
        writeDocument(document);
    }

    @Override
    public void show(Group group) throws Exception {
        System.out.println("работает GroupDAOsax - show");
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        String fileName = "ContactBook.xml";
        User user = new User();

        DefaultHandler handler = new DefaultHandler() {
            boolean isID = false;
            boolean isFirstName = false;
            boolean isLastName = false;
            boolean isAddress = false;
            boolean isPhoneNumber = false;
            boolean isGroup = false;

            // Метод вызывается когда SAXParser "натыкается" на начало тэга
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if (qName.equalsIgnoreCase("ID")) isID = true;
                else if (qName.equalsIgnoreCase("FirstName")) isFirstName = true;
                else if (qName.equalsIgnoreCase("LastName")) isLastName = true;
                else if (qName.equalsIgnoreCase("Address")) isAddress = true;
                else if (qName.equalsIgnoreCase("PhoneNumber")) isPhoneNumber = true;
                else if (qName.equalsIgnoreCase("Group")) isGroup = true;
            }

            // Метод вызывается когда SAXParser считывает текст между тэгами
            @Override
            public void characters(char ch[], int start, int length) throws SAXException {
                String result = new String(ch, start, length);

                if (isID) {
                    long id = Long.parseLong(result);
                    user.setId(id);
                    isID = false;
                } else if (isFirstName) {
                    user.setFname(result);
                    isFirstName = false;
                } else if (isLastName) {
                    user.setLname(result);
                    isLastName = false;
                } else if (isAddress) {
                    user.setAddress(result);
                    isAddress = false;
                } else if (isPhoneNumber) {
                    int phoneNimber = Integer.parseInt(result);
                    user.setPhoneNumber(phoneNimber);
                    isPhoneNumber = false;
                } else if (isGroup) {
                    user.setGroup(result);
                    isGroup = false;
                    if (result.equals(group.getNameGroup()))
                        System.out.println(user);
                }
            }
        };

        // Стартуем разбор методом parse, которому передаем наследника от DefaultHandler, который будет вызываться в нужные моменты
        saxParser.parse(fileName, handler);
    }

    @Override
    public void showAll() throws Exception {
        System.out.println("работает GroupDAOsax - showAll");
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        String fileName = "ContactBook.xml";
        HashSet<String> groupNames = new HashSet<>();

        DefaultHandler handler = new DefaultHandler() {
            boolean isGroup = false;

            // Метод вызывается когда SAXParser "натыкается" на начало тэга
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if (qName.equalsIgnoreCase("Group")) isGroup = true;
            }

            // Метод вызывается когда SAXParser считывает текст между тэгами
            @Override
            public void characters(char ch[], int start, int length) throws SAXException {
                String result = new String(ch, start, length);

                if (isGroup) {
                    groupNames.add(result);
                    isGroup = false;
                }
            }
        };

        // Стартуем разбор методом parse, которому передаем наследника от DefaultHandler, который будет вызываться в нужные моменты
        saxParser.parse(fileName, handler);
        System.out.println(groupNames);
    }

    @Override
    public void searchUser(String name) throws Exception {

    }

    @Override
    public void label(long id, String nameOfGroup) throws WrongIDFormat {

    }

    @Override
    public void deleteLabel(long id) throws WrongIDFormat {

    }

    @Override
    public void setObserver(Observer o) {

    }

    @Override
    public void setData(Data data) {

    }
}
